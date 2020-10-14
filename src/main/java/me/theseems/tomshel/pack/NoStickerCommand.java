package me.theseems.tomshel.pack;

import me.theseems.tomshel.TomasBot;
import me.theseems.tomshel.command.AdminRestricted;
import me.theseems.tomshel.command.SimpleCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NoStickerCommand extends SimpleCommand implements AdminRestricted {
  public NoStickerCommand() {
    super(
        SimpleCommandMeta.onLabel("sticksoff")
            .aliases("offstickers", "banstickers", "банстик", "banstick")
            .description("Запрещать извергам слать стикеры."));
  }

  @Override
  public void handle(TomasBot bot, String[] args, Update update) {
    bot.getChatStorage().setNoStickerMode(!bot.getChatStorage().isNoStickerMode());

    String statusText = (bot.getChatStorage().isNoStickerMode() ? "_ВКЛЮЧЕН_" : "выключен");
    bot.sendBack(
        update,
        new SendMessage()
            .setText("*Режим блокировки стикеров*: " + statusText)
            .enableMarkdown(true));
  }
}
