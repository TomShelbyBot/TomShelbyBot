package me.theseems.tomshelby.bootstrap.builtin;

import me.theseems.tomshelby.Main;
import me.theseems.tomshelby.bootstrap.InitBootstrap;
import me.theseems.tomshelby.config.AccessConfig;
import me.theseems.tomshelby.config.BotConfig;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class ConfigBootstrap implements InitBootstrap {
  private static File configFile;

  public ConfigBootstrap() {
    configFile = new File(Main.getBaseDir(), "config.json");
  }

  private BotConfig loadConfig() {
    try {
      return Main.getGson().fromJson(new FileReader(configFile), BotConfig.class);
    } catch (FileNotFoundException ignored) {
    }
    return null;
  }

  @Override
  public void apply(Logger logger) {
    BotConfig config = loadConfig();
    if (config == null) {
      BotConfig empty =
          new BotConfig(new AccessConfig("YOUR_BOT_USERNAME", "YOUR_BOT_TOKEN_FROM_BOTFATHER"));

      try {
        FileWriter writer = new FileWriter(configFile);
        Main.getGson().toJson(empty, writer);
        writer.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }

      logger.fatal("Please, fill in the config.json");
      logger.fatal("Bot can't start without it on board");
      System.exit(1);
    }

    Main.setBotConfig(config);
  }

  @Override
  public String getInitName() {
    return "Loading config";
  }
}
