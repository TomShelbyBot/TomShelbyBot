package me.theseems.tomshelby.poll;

import org.telegram.telegrambots.meta.api.objects.Update;

public class MessageIdentity {
  private long chatId;
  private int messageId;

  public MessageIdentity(long chatId, int messageId) {
    this.chatId = chatId;
    this.messageId = messageId;
  }

  public static MessageIdentity on(Update update) {
    return new MessageIdentity(update.getMessage().getChatId(), update.getMessage().getMessageId());
  }

  public long getChatId() {
    return chatId;
  }

  public void setChatId(long chatId) {
    this.chatId = chatId;
  }

  public int getMessageId() {
    return messageId;
  }

  public void setMessageId(int messageId) {
    this.messageId = messageId;
  }
}
