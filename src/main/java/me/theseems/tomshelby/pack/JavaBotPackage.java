package me.theseems.tomshelby.pack;

import me.theseems.tomshelby.Main;
import me.theseems.tomshelby.ThomasBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collections;
import java.util.List;

public abstract class JavaBotPackage implements BotPackage {
  private BotPackageConfig config;
  protected ThomasBot bot;

  private void setConfig(BotPackageConfig config) {
    this.config = config;
  }

  private void setBot(ThomasBot bot) {
    this.bot = bot;
  }

  public ThomasBot getBot() {
    return bot;
  }

  @Override
  public BotPackageInfo getInfo() {
    return new BotPackageInfo() {
      @Override
      public String getName() {
        return config.getName();
      }

      @Override
      public String getAuthor() {
        return config.getAuthor();
      }

      @Override
      public String getVersion() {
        return config.getVersion();
      }

      @Override
      public String getDescription() {
        return config.getDescription();
      }

      @Override
      public List<String> getDependencies() {
        List<String> dependencies = config.getDependencies();
        return dependencies == null ? Collections.emptyList() : dependencies;
      }
    };
  }

  public File getPackageFolder() {
    File packsDir = new File(Main.getBaseDir(), "packs");
    return new File(packsDir, getInfo().getName());
  }

  public Logger getLogger() {
    return LoggerFactory.getLogger("Pack '" + getInfo().getName() + "'");
  }

  /** On plugin load */
  public void onLoad() {}

  /** On plugin enable */
  public void onEnable() {}

  /** On plugin disable */
  public void onDisable() {}
}
