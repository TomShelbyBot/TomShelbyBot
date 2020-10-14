package me.theseems.tomshel.pack;

import me.theseems.tomshel.Main;
import me.theseems.tomshel.TomasBot;
import me.theseems.tomshel.command.SimpleCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class InfoCommand extends SimpleCommand {

  public InfoCommand() {
    super(
        SimpleCommandMeta.onLabel("info").alias("start").description("Основаня информация о боте"));
  }

  @Override
  public void handle(TomasBot bot, String[] args, Update update) {
    bot.sendBack(
        update,
        new SendMessage()
            .setText(
                "Да я как Томас Шелби.\nВерсия: "
                    + Main.TOM_BOT_VERSION
                    + "\nАвтор: @theseems и группа Пики Блайндерс"));
  }
}
