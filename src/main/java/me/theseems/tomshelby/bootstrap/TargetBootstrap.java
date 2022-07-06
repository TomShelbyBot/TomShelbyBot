package me.theseems.tomshelby.bootstrap;

import me.theseems.tomshelby.ThomasBot;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public interface TargetBootstrap {
  void apply(Logger logger, ThomasBot bot) throws Exception;

  default String getTargetName() {
    return getClass().getSimpleName();
  }
}
