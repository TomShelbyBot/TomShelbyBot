package me.theseems.tomshelby.config;

public class BotConfig {
  private final AccessConfig accessConfig;

  public BotConfig(AccessConfig accessConfig) {
    this.accessConfig = accessConfig;
  }

  public AccessConfig getAccessConfig() {
    return accessConfig;
  }
}
