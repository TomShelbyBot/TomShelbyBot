package me.theseems.tomshelby.pack;

import me.theseems.tomshelby.Main;
import me.theseems.tomshelby.ThomasBot;

import java.io.File;

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
      public String getDescription() {
        return config.getDescription();
      }
    };
  }

  public File getPackageFolder() {
    File baseDir = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath())
        .getParentFile();
    File packsDir = new File(baseDir, "packs");
    return new File(packsDir, getInfo().getName());
  }

  /** On plugin load */
  public void onLoad() {}

  /** On plugin enable */
  public abstract void onEnable();

  /** On plugin disable */
  public abstract void onDisable();
}
