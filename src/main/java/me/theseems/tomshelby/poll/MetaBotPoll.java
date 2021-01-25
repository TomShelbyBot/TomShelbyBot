package me.theseems.tomshelby.poll;

import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.storage.TomMeta;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;

import java.util.function.Supplier;

public class MetaBotPoll implements BotPoll {
  private static final String META_CHAT_ID = "pollChatId";
  private static final String META_MESSAGE_ID = "pollMessageId";

  private final TomMeta meta;
  private final String id;

  public MetaBotPoll(String id, TomMeta meta) {
    this.meta = meta;
    this.id = id;
  }

  public static void sendAndRegister(ThomasBot bot, SendPoll sendPoll, TomMeta meta) {
    try {
      bot.executeAsync(sendPoll, new SentCallback<Message>() {
        @Override
        public void onResult(BotApiMethod<Message> botApiMethod, Message message) {
          meta.set(META_CHAT_ID, message.getChatId());
          meta.set(META_MESSAGE_ID, message.getMessageId());
          bot.getPollManager().getStorage().put(new MetaBotPoll(message.getPoll().getId(), meta));
        }

        @Override
        public void onError(BotApiMethod<Message> botApiMethod, TelegramApiRequestException e) {}
        @Override
        public void onException(BotApiMethod<Message> botApiMethod, Exception e) {}
      });
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  /**
   * Identity (the exact message in exact chat)
   *
   * @return identity
   */
  @Override
  public MessageIdentity getIdentity() {
    Supplier<IllegalStateException> supplier =
        () -> new IllegalStateException("Cannot find identity of poll");

    return new MessageIdentity(
        meta.getLong(META_CHAT_ID).orElseThrow(supplier),
        meta.getInteger(META_MESSAGE_ID).orElseThrow(supplier));
  }

  /**
   * Get poll id
   *
   * @return id
   */
  @Override
  public String getId() {
    return id;
  }

  /**
   * Get metadata attached to that poll
   *
   * @return meta
   */
  @Override
  public TomMeta getMeta() {
    return meta;
  }
}
