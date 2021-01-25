package me.theseems.tomshelby.bootstrap.builtin;

import me.theseems.tomshelby.Main;
import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.bootstrap.InitBootstrap;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class InitBotBootstrap implements InitBootstrap {
  @Override
  public void apply(Logger logger) {
    Main.setBot(
        new ThomasBot(
            Main.getCommandContainer(),
            Main.getPunishmentStorage(),
            Main.getChatStorage(),
            Main.getPunishmentHandler(),
            Main.getCallbackManager(),
            Main.getUpdateHandlerManager(),
            Main.getBotPackageManager(),
            Main.getPollManager(),
            Main.getBotConfig()));

    try {
      User me = Main.getBot().getMe();
      logger.info("Initialized bot @" + me.getUserName());
    } catch (TelegramApiException e) {
      logger.fatal("Error initializing bot. Please, check your access configuration (config.json)");
      System.exit(1);
    }
  }

  @Override
  public String getInitName() {
    return "Initializing bot";
  }
}
