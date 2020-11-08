package me.theseems.tomshel;

import me.theseems.tomshel.callback.CallbackManager;
import me.theseems.tomshel.command.CommandContainer;
import me.theseems.tomshel.config.BotConfig;
import me.theseems.tomshel.punishment.PunishmentHandler;
import me.theseems.tomshel.storage.ChatStorage;
import me.theseems.tomshel.storage.PunishmentStorage;
import me.theseems.tomshel.update.UpdateHandlerManager;
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

  public ThomasBot(
      CommandContainer commandContainer,
      PunishmentStorage punishmentStorage,
      ChatStorage chatStorage,
      PunishmentHandler punishmentHandler,
      CallbackManager callbackManager,
      UpdateHandlerManager updateHandlerManager,
      BotConfig botConfig) {
    this.commandContainer = commandContainer;
    this.punishmentStorage = punishmentStorage;
    this.chatStorage = chatStorage;
    this.punishmentHandler = punishmentHandler;
    this.callbackManager = callbackManager;
    this.updateHandlerManager = updateHandlerManager;
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

  public BotConfig getBotConfig() {
    return botConfig;
  }

  public void sendBack(Update update, SendMessage message) {
    try {
      message.setChatId(
          update.hasMessage()
              ? update.getMessage().getChatId()
              : update.getCallbackQuery().getMessage().getChatId());
      execute(message);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  public void onUpdateReceived(Update update) {
    try {
      if (update.hasMessage() && update.getMessage().getFrom().getUserName().equals(getBotUsername()))
        return;

      getUpdateHandlerManager().handleUpdate(this, update);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onClosing() {
    Main.save();
  }

  public String getBotUsername() {
    return botConfig.getAccessConfig().getName();
  }

  public String getBotToken() {
    return botConfig.getAccessConfig().getToken();
  }

  @Override
  public String toString() {
    return "ThomasBot{"
        + "commandContainer="
        + commandContainer
        + ", punishmentStorage="
        + punishmentStorage
        + ", chatStorage="
        + chatStorage
        + ", punishmentHandler="
        + punishmentHandler
        + ", callbackManager="
        + callbackManager
        + '}';
  }
}
