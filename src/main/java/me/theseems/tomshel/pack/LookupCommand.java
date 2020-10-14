package me.theseems.tomshel.pack;

import com.google.common.base.Joiner;
import me.theseems.tomshel.TomasBot;
import me.theseems.tomshel.command.SimpleCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ChatMember;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

public class LookupCommand extends SimpleCommand {
  public LookupCommand() {
    super(new SimpleCommandMeta().addAlias("чек"));
  }

  @Override
  public void handle(TomasBot bot, String[] args, Update update) {
    if (args.length == 0) {
      bot.sendBack(
          update,
          new SendMessage()
              .setText("Укажи ник пжж (можно без собачки)")
              .setReplyToMessageId(update.getMessage().getMessageId()));
      return;
    }

    if (args[0].startsWith("@")) args[0] = args[0].substring(1);

    Long chatId = update.getMessage().getChatId();
    Optional<Integer> integer = bot.getChatStorage().lookup(chatId, args[0]);

    if (integer.isPresent()) {
      try {
        ChatMember member =
            bot.execute(new GetChatMember().setUserId(integer.get()).setChatId(chatId));
        bot.sendBack(
            update,
            new SendMessage()
                .setText(
                    "Челик "
                        + Optional.ofNullable(member.getUser().getFirstName()).orElse("<Имя не указано>")
                        + " "
                        + Optional.ofNullable(member.getUser().getLastName()).orElse("<Фамилия не указана>")
                        + " "
                        + member.getUser().getUserName()
                        + " с припиской "
                        + Optional.ofNullable(member.getCustomTitle()).orElse("<Приписки нет>")));
      } catch (TelegramApiException e) {
        bot.sendBack(update, new SendMessage().setText("Произошла ошибочка. Кажется, я не смогу прочекать этого пользователя."));
        e.printStackTrace();
      }
    } else {
      bot.sendBack(update, new SendMessage().setText("Я не нашел такого..("));
      bot.sendBack(
          update,
          new SendMessage()
              .setText(
                  "Из вашего чата я знаю челов ("
                      + bot.getChatStorage().getResolvableUsernames(chatId).size()
                      + "): \n"
                      + Joiner.on(' ')
                          .join(bot.getChatStorage().getResolvableUsernames(chatId))
                          .replace('@', ' ')));
    }
  }

  /**
   * Get label of the command
   *
   * @return label
   */
  @Override
  public String getLabel() {
    return "lookup";
  }
}
