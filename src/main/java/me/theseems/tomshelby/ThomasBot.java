package me.theseems.tomshelby;

import me.theseems.tomshelby.callback.CallbackManager;
import me.theseems.tomshelby.command.CommandContainer;
import me.theseems.tomshelby.config.BotConfig;
import me.theseems.tomshelby.pack.BotPackageManager;
import me.theseems.tomshelby.poll.PollManager;
import me.theseems.tomshelby.punishment.PunishmentHandler;
import me.theseems.tomshelby.storage.ChatStorage;
import me.theseems.tomshelby.storage.PunishmentStorage;
import me.theseems.tomshelby.update.UpdateHandlerManager;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class ThomasBot extends TelegramLongPollingBot {
  private final CommandContainer commandContainer;
  private final PunishmentStorage punishmentStorage;
  private final ChatStorage chatStorage;
  private final PunishmentHandler punishmentHandler;
  private final CallbackManager callbackManager;
  private final UpdateHandlerManager updateHandlerManager;
  private final BotConfig botConfig;
  private final BotPackageManager packageManager;
  private final PollManager pollManager;

  public ThomasBot(
      CommandContainer commandContainer,
      PunishmentStorage punishmentStorage,
      ChatStorage chatStorage,
      PunishmentHandler punishmentHandler,
      CallbackManager callbackManager,
      UpdateHandlerManager updateHandlerManager,
      BotPackageManager botPackageManager,
      PollManager pollManager,
      BotConfig botConfig) {
    this.commandContainer = commandContainer;
    this.punishmentStorage = punishmentStorage;
    this.chatStorage = chatStorage;
    this.punishmentHandler = punishmentHandler;
    this.callbackManager = callbackManager;
    this.updateHandlerManager = updateHandlerManager;
    this.packageManager = botPackageManager;
    this.pollManager = pollManager;
    this.botConfig = botConfig;
  }

  public PunishmentHandler getPunishmentHandler() {
    return punishmentHandler;
  }

  public CommandContainer getCommandContainer() {
    return commandContainer;
  }

  public ChatStorage getChatStorage() {
    return chatStorage;
  }

  public PunishmentStorage getPunishmentStorage() {
    return punishmentStorage;
  }

  public CallbackManager getCallbackManager() {
    return callbackManager;
  }

  public UpdateHandlerManager getUpdateHandlerManager() {
    return updateHandlerManager;
  }

  public BotPackageManager getPackageManager() {
    return packageManager;
  }

  public PollManager getPollManager() {
    return pollManager;
  }

  public String getBotUsername() {
    return botConfig.getAccessConfig().getName();
  }

  public String getBotToken() {
    return botConfig.getAccessConfig().getToken();
  }

  public void onUpdateReceived(Update update) {
    try {
      // Prevent handling own messages
      if (update.hasMessage()
          && update.getMessage().getFrom().getUserName() != null
          && update.getMessage().getFrom().getUserName().equals(getBotUsername())) return;

      getUpdateHandlerManager().handleUpdate(this, update);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void sendBack(Update update, SendMessage message) {
    message.setChatId(
            String.valueOf(update.hasMessage()
                ? update.getMessage().getChatId()
                : update.getCallbackQuery().getMessage().getChatId()));
    try {
      execute(message);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  public void replyBack(Update update, SendMessage message) {
    if (update.hasMessage()) {
      message.setChatId(String.valueOf(update.getMessage().getChatId()));
      if (update.getMessage().hasText()) {
        message.setReplyToMessageId(update.getMessage().getMessageId());
      }
    } else {
      message.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
    }

    try {
      execute(message);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  public void replyBackText(Update update, String message) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setText(message);
    replyBack(update, sendMessage);
  }

  @Override
  public void onClosing() {
    Main.save();
  }

  @Override
  public String toString() {
    return "ThomasBot{" +
        "commandContainer=" + commandContainer +
        ", punishmentStorage=" + punishmentStorage +
        ", chatStorage=" + chatStorage +
        ", punishmentHandler=" + punishmentHandler +
        ", callbackManager=" + callbackManager +
        ", updateHandlerManager=" + updateHandlerManager +
        ", botConfig=" + botConfig +
        ", packageManager=" + packageManager +
        ", pollManager=" + pollManager +
        '}';
  }
}
