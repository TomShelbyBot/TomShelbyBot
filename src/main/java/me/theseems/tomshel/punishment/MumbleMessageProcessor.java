package me.theseems.tomshel.punishment;

import me.theseems.tomshel.Main;
import me.theseems.tomshel.ThomasBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MumbleMessageProcessor implements PunishmentProcessor {
  private Map<Integer, Instant> latest;

  public MumbleMessageProcessor() {
    latest = new ConcurrentHashMap<>();
  }

  @Override
  public boolean handle(Update update, Punishment punishment) {
    ThomasBot bot = Main.getBot();
    try {

      bot.execute(
          new DeleteMessage()
              .setChatId(update.getMessage().getChatId())
              .setMessageId(update.getMessage().getMessageId()));

      if (!latest.containsKey(update.getMessage().getFrom().getId())
          || latest.get(update.getMessage().getFrom().getId()).isBefore(new Date().toInstant())) {
        int length = update.getMessage().hasText() ? update.getMessage().getText().length() : 1;
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
          builder.append(i % 2 == 0 ? "у" : "м");
        }

        bot.sendBack(
            update,
            new SendMessage()
                .setText(
                    "Кажется, @"
                        + update.getMessage().getFrom().getUserName()
                        + " пытался сказать что-то: "
                        + builder.toString()));

        latest.put(
            update.getMessage().getFrom().getId(),
            new Date().toInstant().plus(10, ChronoUnit.SECONDS));
      }
    } catch (TelegramApiException e) {
      bot.sendBack(
          update,
          new SendMessage()
              .setText("А этот рептивый! Кляп вылетает!!!")
              .setReplyToMessageId(update.getMessage().getMessageId()));
    }

    return false;
  }

  @Override
  public PunishmentType getType() {
    return PunishmentType.CLAP_MUTE;
  }

  @Override
  public String getName() {
    return "MumbleMessage";
  }

  @Override
  public int getPriority() {
    return 1000;
  }
}
