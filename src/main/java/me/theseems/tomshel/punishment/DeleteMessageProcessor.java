package me.theseems.tomshel.punishment;

import me.theseems.tomshel.Main;
import me.theseems.tomshel.ThomasBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class DeleteMessageProcessor implements PunishmentProcessor {
  @Override
  public void handle(Update update, Punishment punishment) {
    ThomasBot bot = Main.getBot();
    System.out.println("[" + getName() + "]: Received for handling " + update);
    try {
      bot.executeAsync(
          new DeleteMessage()
              .setChatId(update.getMessage().getChatId())
              .setMessageId(update.getMessage().getMessageId()),
          null);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  @Override
  public PunishmentType getType() {
    return PunishmentType.MUTE;
  }

  @Override
  public String getName() {
    return "DeleteMessage";
  }

  @Override
  public int getPriority() {
    return 1000;
  }
}
