package me.theseems.tomshelby.bootstrap.builtin;

import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.bootstrap.TargetBootstrap;
import me.theseems.tomshelby.command.builtin.HelpBotCommand;
import me.theseems.tomshelby.command.builtin.IdBotCommand;
import me.theseems.tomshelby.command.builtin.InfoBotCommand;
import org.apache.logging.log4j.Logger;


public class AttachCommandsBootstrap implements TargetBootstrap {
  @Override
  public void apply(Logger logger, ThomasBot bot) {
    // Builtin commands
    bot.getCommandContainer()
        .attach(new HelpBotCommand())
        .attach(new IdBotCommand())
        .attach(new InfoBotCommand());
  }

  @Override
  public String getTargetName() {
    return "Attaching builtin commands";
  }
}
