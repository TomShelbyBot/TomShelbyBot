package me.theseems.tomshelby.bootstrap.builtin;

import me.theseems.tomshelby.Main;
import me.theseems.tomshelby.bootstrap.InitBootstrap;
import me.theseems.tomshelby.pack.JarBotPackageManager;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

public class LoadPackagesBootstrap implements InitBootstrap {
  private static JarBotPackageManager jarBotPackageManager;

  @Override
  public void apply(Logger logger) {
    try {
      logger.info("Loading packages...");
      jarBotPackageManager = new JarBotPackageManager();
      File packageDir = new File(Main.getBaseDir(), "packs");
      if (!packageDir.exists()) {
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
}
