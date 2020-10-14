package me.theseems.tomshel.pack;

import me.theseems.tomshel.TomasBot;
import me.theseems.tomshel.command.SimpleCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class GooseCommand extends SimpleCommand {
  public GooseCommand() {
    super(new SimpleCommandMeta().addAlias("гусь").addAlias("guse"));
  }

  /**
   * Handle update for that command
   *
   * @param bot to handle with
   * @param update to handle
   */
  @Override
  public void handle(TomasBot bot, String[] args, Update update) {
    bot.sendBack(
        update,
        new SendMessage()
            .setText(
                "ЗАПУСКАЕМ\n"
                    + "░ГУСЯ░▄▀▀▀▄░РАБОТЯГИ░░\n"
                    + "▄███▀░◐░░░▌░░░░░░░\n"
                    + "░░░░▌░░░░░▐░░░░░░░\n"
                    + "░░░░▐░░░░░▐░░░░░░░\n"
                    + "░░░░▌░░░░░▐▄▄░░░░░\n"
                    + "░░░░▌░░░░▄▀▒▒▀▀▀▀▄\n"
                    + "░░░▐░░░░▐▒▒▒▒▒▒▒▒▀▀▄\n"
                    + "░░░▐░░░░▐▄▒▒▒▒▒▒▒▒▒▒▀▄\n"
                    + "░░░░▀▄░░░░▀▄▒▒▒▒▒▒▒▒▒▒▀▄\n"
                    + "░░░░░░▀▄▄▄▄▄█▄▄▄▄▄▄▄▄▄▄▄▀▄\n"
                    + "░░░░░░░░░░░▌▌░▌▌░░░░░\n"
                    + "░░░░░░░░░░░▌▌░▌▌░░░░░\n"
                    + "░░░░░░░░░▄▄▌▌▄▌▌░░░░░"));
  }

  /**
   * Get label of the command
   *
   * @return label
   */
  @Override
  public String getLabel() {
    return "goose";
  }
}
