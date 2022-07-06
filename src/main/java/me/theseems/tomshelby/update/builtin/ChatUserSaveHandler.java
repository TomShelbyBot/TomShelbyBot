package me.theseems.tomshelby.update.builtin;

import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.update.SimpleUpdateHandler;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ChatUserSaveHandler extends SimpleUpdateHandler {
  @Override
  public boolean handleUpdate(ThomasBot bot, Update update) {
    if (!update.hasMessage()) return true;

    Message message = update.getMessage();
    if (!bot.getChatStorage()
        .lookup(String.valueOf(message.getChatId()), message.getFrom().getUserName())
        .isPresent()) {
      bot.getChatStorage()
          .put(String.valueOf(message.getChatId()), message.getFrom().getUserName(), message.getFrom().getId());
    }

    return true;
  }
}
