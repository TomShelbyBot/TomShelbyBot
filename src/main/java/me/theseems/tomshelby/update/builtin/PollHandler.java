package me.theseems.tomshelby.update.builtin;

import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.poll.BotPoll;
import me.theseems.tomshelby.poll.PollUpdate;
import me.theseems.tomshelby.update.SimpleUpdateHandler;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class PollHandler extends SimpleUpdateHandler {
  @Override
  public boolean handleUpdate(ThomasBot bot, Update update) {
    if (!update.hasPoll() && !update.hasPollAnswer()) return true;

    String pollId;
    if (update.hasPoll()) {
      pollId = update.getPoll().getId();
    } else {
      pollId = update.getPollAnswer().getPollId();
    }

    Optional<BotPoll> pollOptional =
        bot.getPollManager()
            .getStorage()
            .getPoll(pollId);

    if (!pollOptional.isPresent()) return true;
    bot.getPollManager().handleUpdate(bot, new PollUpdate(false, pollOptional.get(), update));
    return false;
  }
}
