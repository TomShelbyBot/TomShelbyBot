package me.theseems.tomshelby.command.builtin;

import me.theseems.tomshelby.Main;
import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.command.Command;
import me.theseems.tomshelby.command.SimpleBotCommand;
import org.telegram.telegrambots.meta.api.objects.Update;

@Command(label = "info", description = "Информация о боте")
public class InfoBotCommand extends SimpleBotCommand {
  private static final String INFO_TEMPLATE =
      "Томас v"
          + Main.TOM_BOT_VERSION
          + "\n"
          + "Автор: @theseems"
          + "\n"
          + "GitHub: https://github.com/TomShelbyBot/TomShelbyBot";

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    bot.replyBackText(update, INFO_TEMPLATE);
  }
}
