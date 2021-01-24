package me.theseems.tomshelby.bootstrap.builtin;

import me.theseems.tomshelby.bootstrap.InitBootstrap;
import org.slf4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;


public class TelegramSdkBootstrap implements InitBootstrap {
  @Override
  public void apply(Logger logger) {
    logger.info("Loading Java SDK for Telegram API");
    ApiContextInitializer.init();
  }
}
