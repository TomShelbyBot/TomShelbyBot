package me.theseems.tomshel.pack;

import com.google.common.base.Joiner;
import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.command.AdminRestricted;
import me.theseems.tomshel.command.SimpleCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.PinChatMessage;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;

public class GoCommand extends SimpleCommand implements AdminRestricted {
  public GoCommand() {
    super(
        SimpleCommandMeta.onLabel("go")
            .aliases("lets", "погнали")
            .description("Отправит вопрос со слов 'Погнали ...'."));
  }

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    try {
      SendPoll sendPoll =
          new SendPoll()
              .setChatId(update.getMessage().getChatId())
              .setQuestion("Погнали " + Joiner.on(' ').join(args))
              .setAnonymous(false)
              .setOptions(Arrays.asList("Да", "Нет", "Идите нахуй"));

      int pollMessageId = bot.execute(sendPoll).getMessageId();
      bot.execute(
          new PinChatMessage()
              .setChatId(update.getMessage().getChatId())
              .setMessageId(pollMessageId));

    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }
}
