package me.theseems.tomshel.pack.dev;

import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.command.BotCommand;
import me.theseems.tomshel.command.SimpleBotCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

public class FatherExportBotCommand extends SimpleBotCommand implements DevPermissibleBotCommand {
  /** A command for exporting all current commands to format that BotFather accepts */
  public FatherExportBotCommand() {
    super(new SimpleCommandMeta().label("fatherexport"));
  }

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    StringBuilder builder = new StringBuilder();
    Collection<BotCommand> filteredCommands =
        bot.getCommandContainer().getCommands().stream()
            .filter(
                botCommand ->
                    botCommand.getMeta() != null
                        && botCommand.getMeta().getLabel() != null
                        && botCommand.getMeta().getDescription()
                            != null) // Filtering commands to have a label and description
            .sorted(
                Comparator.comparing(
                    o -> o.getMeta().getLabel())) // Sorting by lexicographical order
            .collect(Collectors.toList());

    builder.append("```").append('\n');
    for (BotCommand command : filteredCommands) {
      builder
          .append(command.getMeta().getLabel())
          .append(" - ")
          .append(command.getMeta().getDescription())
          .append('\n');
    }
    builder.append("```");

    bot.replyBack(
        update, new SendMessage().setText(builder.toString()).enableMarkdown(true));
  }
}
