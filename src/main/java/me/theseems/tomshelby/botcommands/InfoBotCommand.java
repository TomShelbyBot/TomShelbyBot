package me.theseems.tomshelby.botcommands;

import me.theseems.tomshelby.Main;
import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.command.SimpleBotCommand;
import me.theseems.tomshelby.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.objects.Update;

public class InfoBotCommand extends SimpleBotCommand {

  public InfoBotCommand() {
    super(SimpleCommandMeta.onLabel("info").description("Основаня информация о боте"));
  }

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    bot.replyBackText(
        update,
        "Бот Томас.\nВерсия: "
            + Main.TOM_BOT_VERSION
            + "\n\nАвтор: @theseems\nРепозиторий: https://github.com/TomShelbyBot/TomShelbyBot");
  }
}
