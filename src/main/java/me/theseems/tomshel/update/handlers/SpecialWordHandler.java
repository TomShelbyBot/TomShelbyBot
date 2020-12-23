package me.theseems.tomshel.update.handlers;

import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.punishment.Punishment;
import me.theseems.tomshel.update.SimpleUpdateHandler;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class SpecialWordHandler extends SimpleUpdateHandler {
  /**
   * Handle update
   *
   * @param bot to handle for
   * @param update to handle
   * @return whether we should process update next or not
   */
  @Override
  public boolean handleUpdate(ThomasBot bot, Update update) {
    if (!update.hasMessage()) return true;

    Message message = update.getMessage();
    if (message.getFrom().getId() == 311245296
        && update.getMessage().hasText()
        && update.getMessage().getText().equals("KasayaSabakaVulta")) {

      for (Punishment punishment :
          bot.getPunishmentStorage().getPunishments(message.getFrom().getId())) {
        bot.getPunishmentStorage().removePunishment(message.getFrom().getId(), punishment);
      }

      try {
        bot.execute(
            new DeleteMessage()
                .setMessageId(update.getMessage().getMessageId())
                .setChatId(update.getMessage().getChatId()));
        bot.sendBack(update, new SendMessage().setText("Ок, договорились"));
      } catch (TelegramApiException e) {
        e.printStackTrace();
      }
    }

    return true;
  }
}
