package me.theseems.tomshelby.botcommands.dev.meta;

import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.botcommands.dev.DevPermissibleBotCommand;
import me.theseems.tomshelby.command.CommandMeta;
import me.theseems.tomshelby.command.SimpleCommandMeta;
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
