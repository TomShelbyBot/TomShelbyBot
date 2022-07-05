package me.theseems.tomshelby.bootstrap.builtin;

import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.bootstrap.TargetBootstrap;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


public class TelegramListenBootstrap implements TargetBootstrap {
  @Override
  public void apply(Logger logger, ThomasBot bot) throws TelegramApiException {
    TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
    try {
      telegramBotsApi.registerBot(bot);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String getTargetName() {
    return "Start listening";
  }
}
