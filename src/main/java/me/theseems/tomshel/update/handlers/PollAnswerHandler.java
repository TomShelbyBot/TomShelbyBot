package me.theseems.tomshel.update.handlers;

import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.update.SimpleUpdateHandler;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class PollAnswerHandler extends SimpleUpdateHandler {

  @Override
  public boolean handleUpdate(ThomasBot bot, Update update) {
    if (!update.hasPollAnswer()) return true;

    try {
      String userName =
          update.getPollAnswer().getUser() != null
              ? update.getPollAnswer().getUser().getUserName()
              : "<anon>";
      if (userName == null)
        userName = "Неопознанный импостер <" + update.getPollAnswer().getUser().getId() + ">";

      if (update.getPollAnswer().getOptionIds().contains(0)) {
        bot.execute(
            new SendMessage()
                .setText("\uD83D\uDE18 @" + userName)
                .setChatId(bot.getChatStorage().getChatIds().iterator().next()));
      } else if (update.getPollAnswer().getOptionIds().contains(1)) {
        bot.execute(
            new SendMessage()
                .setText("\uD83D\uDE1E @" + userName)
                .setChatId(bot.getChatStorage().getChatIds().iterator().next()));
      } else if (update.getPollAnswer().getOptionIds().contains(2)) {
        bot.execute(
            new SendMessage()
                .setText("+ ПАШЕЛ НАХУЙ КТО ПРОГОЛОСОВАЛ ЗА ПАШЕЛ НАХУЙ!! СУКА, @" + userName)
                .setChatId(bot.getChatStorage().getChatIds().iterator().next()));
      }
    } catch (Exception ignored) {
    }

    return false;
  }
}
