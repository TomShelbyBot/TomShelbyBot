package me.theseems.tomshelby.bootstrap.builtin;

import me.theseems.tomshelby.Main;
import me.theseems.tomshelby.bootstrap.InitBootstrap;
import me.theseems.tomshelby.pack.JarBotPackageManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class LoadPackagesBootstrap implements InitBootstrap {
  private static JarBotPackageManager jarBotPackageManager;

  // Disable illegal reflection warning as we're using reflection to manage bot packages
  @SuppressWarnings({"rawtypes", "unchecked"})
  private void disableReflectionWarning() {
    try {
      Class unsafeClass = Class.forName("sun.misc.Unsafe");
      Field field = unsafeClass.getDeclaredField("theUnsafe");
      field.setAccessible(true);
      Object unsafe = field.get(null);

      Method putObjectVolatile =
          unsafeClass.getDeclaredMethod(
              "putObjectVolatile", Object.class, long.class, Object.class);
      Method staticFieldOffset = unsafeClass.getDeclaredMethod("staticFieldOffset", Field.class);

      Class loggerClass = Class.forName("jdk.internal.module.IllegalAccessLogger");
      Field loggerField = loggerClass.getDeclaredField("logger");
      Long offset = (Long) staticFieldOffset.invoke(unsafe, loggerField);
      putObjectVolatile.invoke(unsafe, loggerClass, offset, null);
    } catch (Exception ignored) {
    }
  }

  @Override
  public void apply(Logger logger) {
    disableReflectionWarning();

    try {
      jarBotPackageManager = new JarBotPackageManager(LoggerFactory.getLogger("Package Manager"));
      File packageDir = new File(Main.getBaseDir(), "packs");
      if (!packageDir.exists()) {
        //noinspection ResultOfMethodCallIgnored
        packageDir.mkdir();
      }

      jarBotPackageManager.loadPackages(packageDir);
    } catch (IOException e) {
      logger.warn("Error occurred loading packages: " + e.getMessage());
      e.printStackTrace();
    }

    Main.setBotPackageManager(jarBotPackageManager);
  }

  public static JarBotPackageManager getJarBotPackageManager() {
    return jarBotPackageManager;
  }

  @Override
  public String getInitName() {
    return "Loading packages";
  }
}
