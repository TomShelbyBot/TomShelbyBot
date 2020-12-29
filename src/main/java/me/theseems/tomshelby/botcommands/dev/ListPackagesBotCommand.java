package me.theseems.tomshelby.botcommands.dev;

import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.command.SimpleBotCommand;
import me.theseems.tomshelby.command.SimpleCommandMeta;
import me.theseems.tomshelby.pack.JavaBotPackage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ListPackagesBotCommand extends SimpleBotCommand implements DevPermissibleBotCommand {
  public ListPackagesBotCommand() {
    super(
        SimpleCommandMeta.onLabel("listpacks")
            .description("Показать все пакеты, установленные на боте"));
  }

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    StringBuilder builder = new StringBuilder();
    for (JavaBotPackage botPackage : bot.getPackageManager().getPackages()) {
      boolean enabled = bot.getPackageManager().isEnabled(botPackage.getInfo().getName());
      builder
          .append("*")
          .append(botPackage.getInfo().getName())
          .append("*")
          .append('\n')
          .append("|  Статус: ")
          .append(enabled ? "_✔️ (включен)_" : "_❌ (выключен)_")
          .append('\n')
          .append("|  Автор: ")
          .append(botPackage.getInfo().getAuthor())
          .append('\n')
          .append("|  Описание: ")
          .append(botPackage.getInfo().getDescription())
          .append("\n\n");
    }

    bot.replyBack(update, new SendMessage().setText(builder.toString()).enableMarkdown(true));
  }
}
