package me.theseems.tomshel;

import me.theseems.tomshel.callback.CallbackManager;
import me.theseems.tomshel.callback.SimpleCallbackManager;
import me.theseems.tomshel.command.Command;
import me.theseems.tomshel.command.CommandContainer;
import me.theseems.tomshel.command.SimpleCommandContainer;
import me.theseems.tomshel.punishment.Punishment;
import me.theseems.tomshel.punishment.PunishmentHandler;
import me.theseems.tomshel.punishment.SimplePunishmentHandler;
import me.theseems.tomshel.storage.ChatStorage;
import me.theseems.tomshel.storage.PunishmentStorage;
import me.theseems.tomshel.storage.SimpleChatStorage;
import me.theseems.tomshel.storage.SimplePunishmentStorage;
import me.theseems.tomshel.util.CommandUtils;
import me.theseems.tomshel.util.StringUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

public class ThomasBot extends TelegramLongPollingBot {
  private final CommandContainer commandContainer;
  private final PunishmentStorage punishmentStorage;
  private final ChatStorage chatStorage;
  private final PunishmentHandler punishmentHandler;
  private final CallbackManager callbackManager;

  public ThomasBot() {
    this(new SimpleChatStorage());
  }

  public ThomasBot(ChatStorage storage) {
    this.commandContainer = new SimpleCommandContainer();
    this.punishmentStorage = new SimplePunishmentStorage();
    this.punishmentHandler = new SimplePunishmentHandler();
    this.callbackManager = new SimpleCallbackManager();
    this.chatStorage = storage;
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

  public void processUpdate(Update update) {
    if (update.hasCallbackQuery()) {
      callbackManager.call(this, update);
      return;
    }

    Message message = update.getMessage();
    if (!punishmentHandler.handle(update)) return;
    if (update.hasMessage() && message.getFrom().getUserName().equals(getBotUsername())) return;

    handleSpecialWord(update, message);
    handleWelcome(update, message);

    if (!handleNonStickerMode(update)) return;
    if (!message.hasText()) return;
    if (!message.getText().startsWith("/")) return;

    handleCommand(update, message);
  }

  public void onUpdateReceived(Update update) {
    try {
      processUpdate(update);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onClosing() {
    Main.save();
  }

  public String getBotUsername() {
    // return "tomshel_bot";
    return "tom_night_bot";
  }

  public String getBotToken() {
    // return "1322156348:AAFnwWsUneZWmlu-W_oP2MikvntcP56hGmc";
    return "1118855263:AAHy7xNR67KWYfLEjTzBQ5GgFIXl0GCUavs";
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

  /** Simple handlers */
  private void handleSpecialWord(Update update, Message message) {
    if (message.getFrom().getId() == 311245296
        && update.getMessage().hasText()
        && update.getMessage().getText().equals("KasayaSabakaVulta")) {

      for (Punishment punishment : punishmentStorage.getPunishments(message.getFrom().getId())) {
        punishmentStorage.removePunishment(message.getFrom().getId(), punishment);
      }

      try {
        execute(
            new DeleteMessage()
                .setMessageId(update.getMessage().getMessageId())
                .setChatId(update.getMessage().getChatId()));
        sendBack(update, new SendMessage().setText("Ок, договорились"));
      } catch (TelegramApiException e) {
        e.printStackTrace();
      }
    }
  }

  private void handleWelcome(Update update, Message message) {
    if (!chatStorage.lookup(message.getChatId(), message.getFrom().getUserName()).isPresent()) {
      chatStorage.put(
          message.getChatId(), message.getFrom().getUserName(), message.getFrom().getId());
      sendBack(
          update,
          new SendMessage()
              .setText(
                  "Привет, " + message.getFrom().getUserName() + "! Приятно познакомиться :)"));
      Main.save();
    }
  }

  private boolean handleNonStickerMode(Update update) {
    if (chatStorage.isNoStickerMode() && update.getMessage().hasSticker()) {
      try {
        execute(
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

  private void handleCommand(Update update, Message message) {
    String[] args = message.getText().split(" ");
    String label = args[0].substring(1);
    if (label.endsWith(getBotUsername())) {
      label = label.substring(0, Math.max(1, label.length() - 12));
    }

    Optional<Command> commandOptional = commandContainer.get(label);
    if (!commandOptional.isPresent()) return;

    Command command = commandOptional.get();
    if (commandContainer.isAccessible(label, message.getChatId(), message.getFrom().getId())) {

      try {
        command.handle(this, StringUtils.skipOne(args), update);
      } catch (CommandUtils.BotCommandException e) {
        sendBack(update, new SendMessage().setText(e.getMessage()));
      } catch (Exception e) {
        sendBack(
            update, new SendMessage().setText("Мозг сломался. Я не смог обработать эту комманду."));
        e.printStackTrace();
      }

    } else {
      sendBack(
          update,
          new SendMessage()
              .setText("К сожалению, вы не можете использовать эту комманду!")
              .setReplyToMessageId(message.getMessageId()));
    }
  }
}
