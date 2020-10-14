package me.theseems.tomshel.pack;

import me.theseems.tomshel.TomasBot;
import me.theseems.tomshel.command.SimpleCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TestCommand extends SimpleCommand {

  public TestCommand() {
    super(new SimpleCommandMeta().addAlias("tst").addAlias("tetti"));
  }

  /**
   * Handle update for that command
   *
   * @param update to handle
   */
  @Override
  public void handle(TomasBot bot, String[] args, Update update) {
    bot.sendBack(
        update,
        new SendMessage().setText("There you go boiii. You have executed the test command!"));
  }

  /**
   * Get label of the command
   *
   * @return label
   */
  @Override
  public String getLabel() {
    return "test";
  }
}