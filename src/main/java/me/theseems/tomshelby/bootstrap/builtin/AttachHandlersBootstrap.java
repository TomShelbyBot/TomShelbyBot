package me.theseems.tomshelby.bootstrap.builtin;

import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.bootstrap.TargetBootstrap;
import me.theseems.tomshelby.update.SimpleUpdateHandler;
import me.theseems.tomshelby.update.builtin.*;
import org.apache.logging.log4j.Logger;

public class AttachHandlersBootstrap implements TargetBootstrap {
  @Override
  public void apply(Logger logger, ThomasBot bot) {
    // Builtin handlers
    SimpleUpdateHandler.putConsecutively(
        bot,
        new CallbackQueryHandler(),
        new InlineQueryHandler(),
        new PunishmentHandler(),
        new ChatUserSaveHandler(),
        new PollHandler(),
        new CommandHandler());
  }

  @Override
  public String getTargetName() {
    return "Attaching builtin handlers";
  }
}
