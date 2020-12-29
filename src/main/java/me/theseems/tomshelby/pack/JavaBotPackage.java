package me.theseems.tomshelby.pack;

import me.theseems.tomshelby.ThomasBot;

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

  /**
   * On plugin load
   */
  public abstract void onLoad();

  /**
   * On plugin enable
   */
  public abstract void onEnable();

  /**
   * On plugin disable
   */
  public abstract void onDisable();
}
