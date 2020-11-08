package me.theseems.tomshel.config;

public class BotConfig {
  private AccessConfig accessConfig;

  public BotConfig(AccessConfig accessConfig) {
    this.accessConfig = accessConfig;
  }

  public AccessConfig getAccessConfig() {
    return accessConfig;
  }
}
