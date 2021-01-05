package me.theseems.tomshelby.update.builtin;

import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.update.SimpleUpdateHandler;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CallbackQueryHandler extends SimpleUpdateHandler {
  @Override
  public boolean handleUpdate(ThomasBot bot, Update update) {
    if (!update.hasCallbackQuery()) return true;

    bot.getCallbackManager().call(bot, update);
    return false;
  }
}
