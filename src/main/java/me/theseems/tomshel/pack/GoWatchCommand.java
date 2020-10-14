package me.theseems.tomshel.pack;

import com.google.common.base.Joiner;
import me.theseems.tomshel.TomasBot;
import me.theseems.tomshel.command.OnlyAdminRestricted;
import me.theseems.tomshel.command.SimpleCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import me.theseems.tomshel.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.PinChatMessage;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;

public class GoWatchCommand extends SimpleCommand implements OnlyAdminRestricted {
  public GoWatchCommand() {
    super(new SimpleCommandMeta().addAlias("lets"));
  }

  /**
   * Handle update for that command
   *
   * @param bot to handle with
   * @param args
   * @param update to handle
   */
  @Override
  public void handle(TomasBot bot, String[] args, Update update) {
    try {
      int timeout;
      boolean skip = false;

      try {
        timeout = Integer.parseInt(args[0]);
        skip = true;
      } catch (NumberFormatException e) {
        timeout = 0;
      }

      SendPoll sendPoll =
          new SendPoll()
              .setChatId(update.getMessage().getChatId())
              .setQuestion(
                  "Погнали " + Joiner.on(' ').join((skip ? StringUtils.skipOne(args) : args)))
              .setAnonymous(false)
              .setOptions(Arrays.asList("Да", "Нет", "Идите нахуй"));

      if (timeout != 0) {
        sendPoll.setCloseDate(((int) System.currentTimeMillis()) + 1000 * 60 * timeout);
      }

      long pollMessageId = bot.execute(sendPoll).getMessageId();
      bot.execute(new PinChatMessage().setChatId(pollMessageId));

    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  /**
   * Get label of the command
   *
   * @return label
   */
  @Override
  public String getLabel() {
    return "go";
  }
}
