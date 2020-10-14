package me.theseems.tomshel.pack;

import com.google.common.base.Joiner;
import me.theseems.tomshel.TomasBot;
import me.theseems.tomshel.command.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class HelpCommand extends SimpleCommand {
  public HelpCommand() {
    super(
        SimpleCommandMeta.onLabel("thelp").alias("помощь").description("Помощь по коммандам бота"));
  }

  @Override
  public void handle(TomasBot bot, String[] args, Update update) {
    CommandContainer container = bot.getCommandContainer();
    StringBuilder response =
        new StringBuilder(
            "Итак.. вот список того, что я умею.. (" + container.getCommands().size() + "): \n\n");

    for (Command command : container.getCommands()) {
      CommandMeta meta = command.getMeta();
      response.append("/").append(meta.getLabel());
      if (!meta.getAliases().isEmpty())
        response
            .append(" (алиасы: ")
            .append("/")
            .append(Joiner.on(", /").join(meta.getAliases()))
            .append(")");

      response.append("\n");
      response.append("- ").append(meta.getDescription()).append("\n\n");
    }

    bot.sendBack(update, new SendMessage().setText(response.toString()));
  }
}
