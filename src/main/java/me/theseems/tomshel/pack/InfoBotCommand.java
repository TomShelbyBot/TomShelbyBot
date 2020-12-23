package me.theseems.tomshel.pack;

import me.theseems.tomshel.Main;
import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.command.SimpleBotCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
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
            + "\n\nАвтор: @theseems\nРепозиторий: https://github.com/TheSeems/TomShelBot");
  }
}
