package me.theseems.tomshel.pack;

import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.command.SimpleCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class WallCommand extends SimpleCommand {
  public WallCommand() {
    super(
        SimpleCommandMeta.onLabel("wall")
            .aliases("стенка", "стена", "stalemate")
            .description("Построить непробиваемую стенку."));
  }

  /**
   * Handle update for that command
   *
   * @param bot to handle with
   * @param update to handle
   */
  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    bot.sendBack(
        update,
        new SendMessage()
            .setText(
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
                    + "█▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄█"));
  }
}
