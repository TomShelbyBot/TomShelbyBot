package me.theseems.tomshel.update.handlers;

import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.update.SimpleUpdateHandler;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class NonStickerModeHandler extends SimpleUpdateHandler {
  /**
   * Handle update
   *
   * @param bot    to handle for
   * @param update to handle
   * @return whether we should process update next or not
   */
  @Override
  public boolean handleUpdate(ThomasBot bot, Update update) {
    if (bot.getChatStorage().isNoStickerMode() && update.getMessage().hasSticker()) {
      try {
        bot.execute(
            new DeleteMessage()
                .setMessageId(update.getMessage().getMessageId())
                .setChatId(update.getMessage().getChatId()));
      } catch (TelegramApiException e) {
        e.printStackTrace();
      }
      return false;
    }
    return true;
  }
}
