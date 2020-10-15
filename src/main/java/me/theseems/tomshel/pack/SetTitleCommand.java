package me.theseems.tomshel.pack;

import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.command.AdminRestricted;
import me.theseems.tomshel.command.SimpleCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.SetChatAdministratorCustomTitle;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ChatMember;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.Optional;

public class SetTitleCommand extends SimpleCommand implements AdminRestricted {
  public SetTitleCommand() {
    super(SimpleCommandMeta.onLabel("title")
        .aliases("settitle", "кличка", "погоняло", "титул")
        .description("Выдать кличку."));
  }

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    if (args.length < 2) {
      bot.sendBack(
          update, new SendMessage().setText("Укажите юзера кому нужно выдать титул и сам титул!"));
    } else {
      if (args[0].startsWith("@")) args[0] = args[0].substring(1);

      Optional<Integer> userIdOptional =
          bot.getChatStorage().lookup(update.getMessage().getChatId(), args[0]);

      if (!userIdOptional.isPresent()) {
        bot.sendBack(update, new SendMessage().setText("Не нашел этого гражданина, сорри."));
        return;
      }

      Integer userId = userIdOptional.get();
      try {
        ChatMember member =
            bot.execute(
                new GetChatMember().setChatId(update.getMessage().getChatId()).setUserId(userId));
        bot.execute(
            new SetChatAdministratorCustomTitle()
                .setChatId(update.getMessage().getChatId())
                .setUserId(userId)
                .setCustomTitle(args[1]));

        bot.sendBack(
            update,
            new SendMessage()
                .setText(
                    update.getMessage().getFrom().getUserName()
                        + " установил @"
                        + member.getUser().getUserName()
                        + " титул '"
                        + args[1]
                        + "'"));

      } catch (TelegramApiRequestException e) {
        bot.sendBack(
            update,
            new SendMessage()
                .setText(
                    "Не удалось выдать позвыной: "
                        + e.getApiResponse()
                        + " ("
                        + e.getLocalizedMessage()
                        + ")").setReplyToMessageId(update.getMessage().getMessageId()));
      } catch (TelegramApiException e) {
        e.printStackTrace();
      }
    }
  }

}
