package me.theseems.tomshelby.bootstrap.builtin;

import me.theseems.tomshelby.Main;
import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.bootstrap.InitBootstrap;
import me.theseems.tomshelby.bootstrap.TargetBootstrap;
import me.theseems.tomshelby.poll.MetaPollContainer;
import me.theseems.tomshelby.poll.MetaPollManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class PollTargetBootstrap implements InitBootstrap, TargetBootstrap {
  private MetaPollContainer metaPollContainer;

  @Override
  public void apply(org.apache.logging.log4j.Logger logger) {
    metaPollContainer = new MetaPollContainer();
    MetaPollManager metaPollManager = new MetaPollManager(metaPollContainer);
    Main.setPollManager(metaPollManager);
  }

  @Override
  public void apply(Logger logger, ThomasBot bot) {
    metaPollContainer.setThomasBot(bot);
    try {
      metaPollContainer.setSelfChatId(String.valueOf(bot.getMe().getId()));
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String getInitName() {
    return "Initializing poll manager";
  }

  @Override
  public String getTargetName() {
    return "Starting poll manager";
  }
}
