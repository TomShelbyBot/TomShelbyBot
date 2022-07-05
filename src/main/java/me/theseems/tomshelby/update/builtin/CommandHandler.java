package me.theseems.tomshelby.update.builtin;

import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.command.BotCommand;
import me.theseems.tomshelby.update.SimpleUpdateHandler;
import me.theseems.tomshelby.util.CommandUtils;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class CommandHandler extends SimpleUpdateHandler {

  public CommandHandler() {
    // We assume command handler to be the latest (applying to core, not packages) to run
    setPriority(1000);
  }

  @Override
  public boolean handleUpdate(ThomasBot bot, Update update) {
    if (!update.hasMessage() || !update.getMessage().hasText()) return true;
    Message message = update.getMessage();

    // Extracting command
    CommandUtils.CommandSkeleton skeleton = CommandUtils.extractCommand(message.getText());
    String label = skeleton.label;
    String[] args = skeleton.args;
    boolean explicit = skeleton.accessExplicit;

    Optional<BotCommand> commandOptional = bot.getCommandContainer().get(label);
    // If we haven't found our command
    if (!commandOptional.isPresent()) return true;

    // When user, for example, calls /help command (many other bots use that command too)
    // If it's marked explicitly accessed and we have just /help we don't react
    // The only exception is private chats (first expression in if statement)
    if (!update.getMessage().getChatId().equals(Long.valueOf(update.getMessage().getFrom().getId()))
        && commandOptional.get().getMeta().isAccessExplicit()
        && !explicit) return false;

    BotCommand botCommand = commandOptional.get();
    // Checking permissions to use command
    if (!bot.getCommandContainer()
        .isAccessible(label, String.valueOf(message.getChatId()), message.getFrom().getId())) {
      bot.replyBackText(update, "Недостаточные права.");
      return false;
    }

    try {
      botCommand.handle(bot, args, update);
    } catch (CommandUtils.BotCommandException e) {
      bot.replyBackText(update, e.getMessage());
    } catch (Exception e) {
      System.err.println(
          "Error occurred while handling command '" + label + "' from update " + update.toString());
      e.printStackTrace();
    }

    return true;
  }
}
