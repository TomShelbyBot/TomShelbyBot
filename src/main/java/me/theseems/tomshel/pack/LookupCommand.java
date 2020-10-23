package me.theseems.tomshel.pack;

import com.google.common.base.Joiner;
import me.theseems.tomshel.ThomasBot;
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
    super(
        SimpleCommandMeta.onLabel("check")
            .aliases("lookup", "who", "чек", "кто")
            .description("Основная информация из сведений семьи Шелби."));
  }

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    if (args.length == 0) {
      bot.sendBack(
          update,
          new SendMessage()
              .setText("Так.. ник-то укажи!")
              .setReplyToMessageId(update.getMessage().getMessageId()));
      return;
    }

    if (args[0].startsWith("@")) args[0] = args[0].substring(1);

    Long chatId = update.getMessage().getChatId();
    Optional<Integer> userId = bot.getChatStorage().lookup(chatId, args[0]);

    if (!userId.isPresent()) {
      bot.sendBack(update, new SendMessage().setText("А вот этого гражданина я не знаю."));
      String knownNicknames =
          Joiner.on(' ')
              .join(bot.getChatStorage().getResolvableUsernames(chatId))
              .replace('@', ' ');

      bot.sendBack(
          update,
          new SendMessage()
              .setText(
                  "Мне известны сведения ("
                      + bot.getChatStorage().getResolvableUsernames(chatId).size()
                      + "): \n"
                      + knownNicknames));
      return;
    }

    try {
      ChatMember member =
          bot.execute(new GetChatMember().setUserId(userId.get()).setChatId(chatId));

      String firstName =
          Optional.ofNullable(member.getUser().getFirstName()).orElse("<Имя не указано>");
      String lastName =
          Optional.ofNullable(member.getUser().getLastName()).orElse("<Фамилия не указана>");
      String title = Optional.ofNullable(member.getCustomTitle()).orElse("<Приписки нет>");

      bot.sendBack(
          update,
          new SendMessage()
              .setText(
                  "Гражданин "
                      + firstName
                      + " "
                      + lastName
                      + " "
                      + member.getUser().getUserName()
                      + " с припиской "
                      + title));

    } catch (TelegramApiException e) {
      bot.sendBack(
          update,
          new SendMessage().setText("Произошла ошибочка. Кажется, я не узнаю этого пользователя."));
      e.printStackTrace();
    }
  }
}
