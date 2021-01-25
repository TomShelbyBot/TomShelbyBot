package me.theseems.tomshelby.bootstrap.builtin;

import me.theseems.tomshelby.bootstrap.InitBootstrap;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;


public class TelegramSdkBootstrap implements InitBootstrap {
  @Override
  public void apply(Logger logger) {
    ApiContextInitializer.init();
  }

  @Override
  public String getInitName() {
    return "Loading Telegram java SDK";
  }
}
