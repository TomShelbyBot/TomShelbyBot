package me.theseems.tomshelby.pack;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import me.theseems.tomshelby.ThomasBot;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

public class JarBotPackageManager implements BotPackageManager {
  private final Map<String, JavaBotPackage> botPackageMap;
  private final Set<String> enabledPackages;

  public JarBotPackageManager() {
    this.botPackageMap = new HashMap<>();
    this.enabledPackages = new HashSet<>();
  }

  private File[] dragJars(File directory) {
    return directory.listFiles(
        pathname -> {
          if (pathname == null) return false;
          if (!pathname.getName().contains(".")) return false;

          String fileName = pathname.getName();
          String extension = fileName.substring(fileName.lastIndexOf('.'));
          return extension.equals(".jar");
        });
  }

  private void enablePack(ThomasBot bot, JavaBotPackage javaBotPackage)
      throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
    // Setting bot
    Method setBot =
        javaBotPackage.getClass().getSuperclass().getDeclaredMethod("setBot", ThomasBot.class);
    setBot.setAccessible(true);
    setBot.invoke(javaBotPackage, bot);
    javaBotPackage.onEnable();
  }

  // Some black magic :(
  private JavaBotPackage injectJarAndInvokeLoad(
      URLClassLoader classLoader, File file, BotPackageConfig config)
      throws IllegalArgumentException {
    URL url;
    try {
      url = file.toURI().toURL();
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException("Cannot inject package into runtime");
    }

    URLClassLoader sysLoader = new URLClassLoader(new URL[0]);
    Method sysMethod;
    try {
      sysMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
      sysMethod.setAccessible(true);
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException("Cannot inject package into runtime");
    }

    try {
      sysMethod.invoke(sysLoader, url);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new IllegalArgumentException("Cannot inject package into runtime");
    }

    try {
      //noinspection rawtypes
      Class clazz = Class.forName(config.getMain(), true, classLoader);
      //noinspection unchecked
      Object object = clazz.getConstructor().newInstance();

      // Setting config
      Method setConfig =
          object.getClass().getSuperclass().getDeclaredMethod("setConfig", BotPackageConfig.class);
      setConfig.setAccessible(true);
      setConfig.invoke(object, config);

      try {
        //noinspection unchecked
        clazz.getDeclaredMethod("onLoad").invoke(object);
      } catch (NoSuchMethodException ignored) {
      } catch (Exception e) {
        throw new IllegalArgumentException("Package failed to load", e);
      }

      return (JavaBotPackage) object;
    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException("Main class was not found", e);
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException("Main class does not have 'onEnable' method", e);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new IllegalArgumentException("Main class 'onEnable' method is inaccessible", e);
    } catch (InstantiationException e) {
      throw new IllegalArgumentException(
          "Main class does not have constructor with no parameters", e);
    }
  }

  private void loadSinglePackage(URLClassLoader classLoader, File file)
      throws IOException, IllegalArgumentException, JsonSyntaxException {
    try (JarFile jarFile = new JarFile(file)) {
      ZipEntry configEntry = jarFile.getEntry("botpackage.json");

      // Check config is in there
      if (configEntry == null) throw new IllegalArgumentException("Cannot find botpackage.json");

      // Read the config's contents
      String botPackageRaw =
          IOUtils.toString(jarFile.getInputStream(configEntry), StandardCharsets.UTF_8);

      // Parsing bot package config
      BotPackageConfig config = new Gson().fromJson(botPackageRaw, BotPackageConfig.class);

      // Check for required fields
      if (config.getMain() == null)
        throw new IllegalArgumentException("Main class is not specified in botpackage.json");
      if (config.getName() == null)
        throw new IllegalArgumentException("Name of package is not specified in botpackage.json");
      if (config.getVersion() == null)
        throw new IllegalArgumentException("Version is not specified in botpackage.json");

      // Check for name conflicts with existing packages
      if (botPackageMap.containsKey(config.getName())) {
        BotPackageInfo present = botPackageMap.get(config.getName()).getInfo();
        throw new IllegalArgumentException(
            "Package with that name already exists: '"
                + present.getName()
                + "' v"
                + present.getVersion()
                + " by "
                + present.getAuthor());
      }

      // Check if 'main' matches the java package regexp
      if (!config.getMain().matches("^(?:\\w+|\\w+\\.\\w+)+$"))
        throw new IllegalArgumentException("Invalid main class specified in botpackage.json");

      // Check if we can find main class entry
      if (jarFile.getJarEntry(config.getMain().replace('.', '/') + ".class") == null)
        throw new IllegalArgumentException("Main class '" + config.getMain() + "' was not found");

      try {
        JavaBotPackage javaBotPackage = injectJarAndInvokeLoad(classLoader, file, config);
        botPackageMap.put(config.getName(), javaBotPackage);
      } catch (Exception e) {
        System.err.println("Error loading plugin '" + config.getName() + "': " + e.getMessage());
        e.printStackTrace();
      }
    }
  }

  public void loadPackages(File directory) throws IOException {
    if (!directory.exists()) throw new IOException("Bot package directory does not exist");
    if (!directory.isDirectory()) throw new IOException("Specified file is not a directory");

    File[] files = dragJars(directory);
    if (files == null) throw new IOException("Cannot iterate through bot package directory");

    List<File> uris = Arrays.asList(files);
    URLClassLoader child =
        new URLClassLoader(
            uris.stream()
                .map(
                    file -> {
                      try {
                        return file.toURI().toURL();
                      } catch (MalformedURLException e) {
                        e.printStackTrace();
                      }
                      return null;
                    })
                .collect(Collectors.toList())
                .toArray(new URL[] {}),
            JavaBotPackage.class.getClassLoader());

    for (File file : files) {
      try {
        loadSinglePackage(child, file);
      } catch (IllegalArgumentException e) {
        // Catching incorrect package exception
        System.err.println("Cannot load package '" + file.getName() + "': " + e.getMessage());
      } catch (Exception e) {
        // Catching general exception as we don't want bot to fail because of it
        System.err.println("Error loading package '" + file.getName() + "': " + e.getMessage());
        e.printStackTrace();
      }
    }
  }

  @Override
  public Collection<BotPackage> getPackages() {
    return new ArrayList<>(botPackageMap.values());
  }

  @Override
  public Optional<BotPackage> getPackageByName(String name) {
    return Optional.ofNullable(botPackageMap.get(name));
  }

  public void enablePackage(ThomasBot bot, String name) {
    if (!botPackageMap.containsKey(name)) return;
    if (enabledPackages.contains(name)) return;
    try {
      enablePack(bot, botPackageMap.get(name));
    } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
      throw new IllegalStateException("Cannot disable pack '" + name + "': " + e.getMessage(), e);
    }
    enabledPackages.add(name);
  }

  @Override
  public void disablePackage(String name) {
    if (!botPackageMap.containsKey(name)) return;
    if (!enabledPackages.contains(name)) return;
    botPackageMap.get(name).onDisable();
    enabledPackages.remove(name);
  }

  @Override
  public boolean isEnabled(String name) {
    return enabledPackages.contains(name);
  }
}
