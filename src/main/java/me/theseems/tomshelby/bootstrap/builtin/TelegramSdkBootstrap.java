package me.theseems.tomshelby.bootstrap.builtin;

import me.theseems.tomshelby.bootstrap.InitBootstrap;
import org.apache.logging.log4j.Logger;

public class TelegramSdkBootstrap implements InitBootstrap {
  @Override
  public void apply(Logger logger) {
    logger.info("dunno");
  }

  @Override
  public String getInitName() {
    return "Loading Telegram java SDK";
  }
}
