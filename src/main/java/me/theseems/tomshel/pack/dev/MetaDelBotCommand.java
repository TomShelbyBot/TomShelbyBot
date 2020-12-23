package me.theseems.tomshel.pack.dev;

import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.command.CommandMeta;
import me.theseems.tomshel.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MetaDelBotCommand implements DevPermissibleBotCommand {

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    if (args.length < 1) bot.sendBack(update, new SendMessage().setText("Укажите ключ"));

    bot.getChatStorage()
        .getChatMeta(update.getMessage().getChatId()).remove(args[0]);
    bot.sendBack(update, new SendMessage().setText("OK"));
  }

  @Override
  public CommandMeta getMeta() {
    return SimpleCommandMeta.onLabel("metadel");
  }
}
