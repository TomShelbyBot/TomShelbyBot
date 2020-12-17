package me.theseems.tomshel.pack;

import me.theseems.tomshel.Main;
import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.command.SimpleBotCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class InfoBotCommand extends SimpleBotCommand {

  public InfoBotCommand() {
    super(
        SimpleCommandMeta.onLabel("info").alias("start").description("Основаня информация о боте"));
  }

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    bot.sendBack(
        update,
        new SendMessage()
            .setText(
                "Да я как Томас Шелби.\nВерсия: "
                    + Main.TOM_BOT_VERSION
                    + "\nАвторы: бать и мать Томаса Шелби\n\nРепозиторий: https://github.com/TheSeems/TomShelBot"));
  }
}
