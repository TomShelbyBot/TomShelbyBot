package me.theseems.tomshelby.bootstrap.builtin;

import me.theseems.tomshelby.Main;
import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.bootstrap.InitBootstrap;
import org.slf4j.Logger;


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
  }
}
