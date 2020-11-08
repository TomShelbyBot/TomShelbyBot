package me.theseems.tomshel.update.handlers;

import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.update.SimpleUpdateHandler;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CallbackQueryHandler extends SimpleUpdateHandler {
  @Override
  public boolean handleUpdate(ThomasBot bot, Update update) {
    if (!update.hasCallbackQuery()) return true;

    bot.getCallbackManager().call(bot, update);
    return false;
  }
}
