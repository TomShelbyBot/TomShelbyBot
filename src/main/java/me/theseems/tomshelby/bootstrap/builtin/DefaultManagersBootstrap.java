package me.theseems.tomshelby.bootstrap.builtin;

import me.theseems.tomshelby.Main;
import me.theseems.tomshelby.bootstrap.InitBootstrap;
import me.theseems.tomshelby.callback.SimpleCallbackManager;
import me.theseems.tomshelby.command.SimpleCommandContainer;
import me.theseems.tomshelby.punishment.SimplePunishmentHandler;
import me.theseems.tomshelby.storage.SimplePunishmentStorage;
import me.theseems.tomshelby.update.SimpleUpdateHandlerManager;
import org.slf4j.Logger;


public class DefaultManagersBootstrap implements InitBootstrap {
  @Override
  public void apply(Logger logger) {
    Main.setCommandContainer(new SimpleCommandContainer());
    Main.setPunishmentStorage(new SimplePunishmentStorage());
    Main.setPunishmentHandler(new SimplePunishmentHandler());
    Main.setCallbackManager(new SimpleCallbackManager());
    Main.setUpdateHandlerManager(new SimpleUpdateHandlerManager());
  }
}
