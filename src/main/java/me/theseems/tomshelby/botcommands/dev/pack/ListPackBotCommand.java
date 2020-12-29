package me.theseems.tomshelby.botcommands.dev.pack;

import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.botcommands.dev.DevPermissibleBotCommand;
import me.theseems.tomshelby.command.SimpleBotCommand;
import me.theseems.tomshelby.command.SimpleCommandMeta;
import me.theseems.tomshelby.pack.BotPackage;
import me.theseems.tomshelby.pack.BotPackageManager;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ListPackBotCommand extends SimpleBotCommand implements DevPermissibleBotCommand {
  public ListPackBotCommand() {
    super(
        SimpleCommandMeta.onLabel("listpacks")
            .description("Показать все пакеты, установленные на боте"));
  }

  public static String getPackageEntry(BotPackageManager manager, BotPackage pack) {
    StringBuilder builder = new StringBuilder();
    boolean isEnabled = manager.isEnabled(pack.getInfo().getName());
    builder
        .append("*")
        .append(pack.getInfo().getName())
        .append("*")
        .append('\n')
        .append("|  Статус: ")
        .append(isEnabled ? "_✔️ (включен)_" : "_❌ (выключен)_")
        .append('\n')
        .append("|  Автор: ")
        .append(pack.getInfo().getAuthor())
        .append('\n')
        .append("|  Описание: ")
        .append(pack.getInfo().getDescription())
        .append("\n\n");
    return builder.toString();
  }

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    StringBuilder builder = new StringBuilder();
    for (BotPackage pack : bot.getPackageManager().getPackages()) {
      builder.append(getPackageEntry(bot.getPackageManager(), pack));
    }

    bot.replyBack(update, new SendMessage().setText(builder.toString()).enableMarkdown(true));
  }
}
