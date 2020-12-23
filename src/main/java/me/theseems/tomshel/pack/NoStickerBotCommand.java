package me.theseems.tomshel.pack;

import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.command.AdminPermissibleBotCommand;
import me.theseems.tomshel.command.SimpleBotCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import me.theseems.tomshel.storage.TomMeta;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NoStickerBotCommand extends SimpleBotCommand implements AdminPermissibleBotCommand {
  public static final String STICKER_MODE_META_KEY = "stickerMode";

  public NoStickerBotCommand() {
    super(
        SimpleCommandMeta.onLabel("stickermode")
            .aliases("sticks", "togglest")
            .description("Запрещать извергам слать стикеры."));
  }

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    TomMeta meta = bot.getChatStorage().getChatMeta(update.getMessage().getChatId());
    boolean current = meta.getBoolean(STICKER_MODE_META_KEY).orElse(false);
    meta.set(STICKER_MODE_META_KEY, !current);

    String statusText = (!current ? "_ВКЛЮЧЕН_" : "выключен");
    bot.sendBack(
        update,
        new SendMessage()
            .setText("*Режим блокировки стикеров*: " + statusText)
            .enableMarkdown(true));
  }
}
