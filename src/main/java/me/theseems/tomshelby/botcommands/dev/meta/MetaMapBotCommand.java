package me.theseems.tomshelby.botcommands.dev.meta;

import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.botcommands.dev.DevPermissibleBotCommand;
import me.theseems.tomshelby.command.CommandMeta;
import me.theseems.tomshelby.command.SimpleCommandMeta;
import me.theseems.tomshelby.storage.TomMeta;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class MetaMapBotCommand implements DevPermissibleBotCommand {

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    if (args.length < 1) bot.sendBack(update, new SendMessage().setText("Укажите чат."));

    try {
      Long chatId = Long.parseLong(args[0]);
      if (!bot.getChatStorage().getChatIds().contains(chatId)) {
        bot.sendBack(update, new SendMessage().setText("Для чата нет сохраненной меты"));
        return;
      }

      TomMeta meta = bot.getChatStorage().getChatMeta(chatId);
      StringBuilder builder = new StringBuilder();

      builder.append("Чат: _").append(chatId).append("_\n");
      builder.append("Количество записей: _").append(meta.getKeys().size()).append("_\n");
      builder.append("```").append('\n');
      for (String key : meta.getKeys()) {
        builder.append('\n');
        builder
            .append("'")
            .append(key)
            .append("'")
            .append(" ⟾ ")
            .append(
                meta.get(key).flatMap(value -> Optional.of("'" + value + "'")).orElse("__null__"));
      }
      builder.append('\n');
      builder.append("```");

      bot.sendBack(update, new SendMessage().setText(builder.toString()).enableMarkdown(true));
    } catch (NumberFormatException e) {
      bot.sendBack(update, new SendMessage().setText("Укажите валидный айди чата."));
    }
  }

  @Override
  public CommandMeta getMeta() {
    return SimpleCommandMeta.onLabel("metamap");
  }
}
