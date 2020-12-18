package me.theseems.tomshel.pack.dev;

import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.command.BotCommand;
import me.theseems.tomshel.command.CommandMeta;
import me.theseems.tomshel.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MetaGetBotCommand extends DevPermissible implements BotCommand {

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    if (args.length < 1) bot.sendBack(update, new SendMessage().setText("Укажите ключ"));

    Object value =
        bot.getChatStorage()
            .getChatMeta(update.getMessage().getChatId())
            .get(args[0])
            .orElse("_<отсутствует>_");
    bot.sendBack(update, new SendMessage().setText(value.toString()));
  }

  @Override
  public CommandMeta getMeta() {
    return SimpleCommandMeta.onLabel("metaget");
  }
}
