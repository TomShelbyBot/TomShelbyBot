package me.theseems.tomshelby.botcommands.dev.meta;

import com.google.common.base.Joiner;
import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.botcommands.dev.DevPermissibleBotCommand;
import me.theseems.tomshelby.command.CommandMeta;
import me.theseems.tomshelby.command.SimpleCommandMeta;
import me.theseems.tomshelby.storage.TomMeta;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MetaPutBotCommand implements DevPermissibleBotCommand {

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    if (args.length < 2) bot.sendBack(update, new SendMessage().setText("Укажите ключ и значение"));

    String key = args[0];
    String value = Joiner.on(' ').join(Arrays.stream(args).skip(1).collect(Collectors.toList()));

    TomMeta meta = bot.getChatStorage().getChatMeta(update.getMessage().getChatId());
    meta.set(key, value);

    bot.sendBack(update, new SendMessage().setText("OK"));
  }

  @Override
  public CommandMeta getMeta() {
    return SimpleCommandMeta.onLabel("metaput");
  }
}
