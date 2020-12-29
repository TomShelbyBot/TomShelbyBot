package me.theseems.tomshelby.botcommands;

import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.command.SimpleBotCommand;
import me.theseems.tomshelby.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.objects.Update;

public class WallBotCommand extends SimpleBotCommand {
  public WallBotCommand() {
    super(SimpleCommandMeta.onLabel("wall").description("Построить непробиваемую стенку."));
  }

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    bot.replyBackText(
        update,
        "СТРОИМ СТЕНУ РАБОТЯГИ!\n"
            + "█▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀█\n"
            + "█═╩═╦═╩═╦═╩═╦═╩═╦═╩═╦═╩═╦═╩═█\n"
            + "█═╦═╩═╦═╩═╦═╩═╦═╩═╦═╩═╦═╩═╦═█\n"
            + "█═╩═╦═╩═╦═╩═╦═╩═╦═╩═╦═╩═╦═╩═█\n"
            + "█═╦═╩═╦═╩═╦═╩═╦═╩═╦═╩═╦═╩═╦═█\n"
            + "█═╩═╦═╩═╦═╩═╦═╩═╦═╩═╦═╩═╦═╩═█\n"
            + "█═╦═╩═╦═╩═╦═╩═╦═╩═╦═╩═╦═╩═╦═█\n"
            + "█═╩═╦═╩═╦═╩═╦═╩═╦═╩═╦═╩═╦═╩═█\n"
            + "█═╦═╩═╦═╩═╦═╩═╦═╩═╦═╩═╦═╩═╦═█\n"
            + "█▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄█");
  }
}
