package me.theseems.tomshel.update.handlers;

import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.command.Command;
import me.theseems.tomshel.update.SimpleUpdateHandler;
import me.theseems.tomshel.util.CommandUtils;
import me.theseems.tomshel.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class CommandHandler extends SimpleUpdateHandler {
  @Override
  public boolean handleUpdate(ThomasBot bot, Update update) {
    if (!update.hasMessage() || !update.getMessage().hasText()) return true;
    Message message = update.getMessage();

    String[] args = update.getMessage().getText().split(" ");
    String label = args[0].substring(1);
    if (label.endsWith(bot.getBotUsername())) {
      label = label.substring(0, Math.max(1, label.length() - 12));
    }

    Optional<Command> commandOptional = bot.getCommandContainer().get(label);
    if (!commandOptional.isPresent()) return true;

    Command command = commandOptional.get();
    if (bot.getCommandContainer()
        .isAccessible(label, message.getChatId(), message.getFrom().getId())) {

      try {
        command.handle(bot, StringUtils.skipOne(args), update);
      } catch (CommandUtils.BotCommandException e) {
        bot.sendBack(update, new SendMessage().setText(e.getMessage()));
      } catch (Exception e) {
        bot.sendBack(
            update, new SendMessage().setText("Мозг сломался. Я не смог обработать эту комманду."));
        e.printStackTrace();
      }

    } else {
      bot.sendBack(
          update,
          new SendMessage()
              .setText("К сожалению, вы не можете использовать эту комманду!")
              .setReplyToMessageId(message.getMessageId()));
    }

    return true;
  }
}
