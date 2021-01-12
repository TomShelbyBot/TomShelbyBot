package me.theseems.tomshelby.command.builtin;

import com.google.common.base.Joiner;
import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.command.*;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HelpBotCommand extends SimpleBotCommand {
  public HelpBotCommand() {
    super(
        SimpleCommandMeta.onLabel("help")
            .alias("start")
            .explicitAccess()
            .description("Помощь по коммандам бота"));
  }

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    CommandContainer container = bot.getCommandContainer();
    StringBuilder response =
        new StringBuilder(
            "Итак.. вот список того, что я умею.. (" + container.getCommands().size() + "): \n\n");

    Collection<BotCommand> commands =
        container.getCommands().stream()
            .filter(
                botCommand ->
                    botCommand != null
                        && botCommand.getMeta().getLabel() != null
                        && botCommand.getMeta().getDescription() != null)
            .sorted(Comparator.comparing(o -> o.getMeta().getLabel()))
            .collect(Collectors.toList());

    for (BotCommand botCommand : commands) {
      CommandMeta meta = botCommand.getMeta();

      // Label part
      response.append("/").append(meta.getLabel());
      if (meta.isAccessExplicit()) {
        response.append("@").append(bot.getBotUsername());
      }

      // Aliases part
      if (!meta.getAliases().isEmpty()) {
        response.append(" (алиасы: ").append("/");

        List<String> parts = new ArrayList<>(meta.getAliases());
        if (meta.isAccessExplicit()) {
          // Add bot username at the end
          parts =
              parts.stream().map(s -> s + "@" + bot.getBotUsername()).collect(Collectors.toList());
        }

        response.append(Joiner.on(", /").join(parts));
        response.append(")");
      }

      // Description part
      response.append("\n");
      response.append("- ").append(meta.getDescription()).append("\n\n");
    }

    bot.replyBackText(update, response.toString());
  }
}
