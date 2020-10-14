package me.theseems.tomshel;

import me.theseems.tomshel.command.CommandContainer;
import me.theseems.tomshel.command.SimpleCommandContainer;
import me.theseems.tomshel.punishment.PunishmentType;
import me.theseems.tomshel.storage.ChatStorage;
import me.theseems.tomshel.storage.PunishmentStorage;
import me.theseems.tomshel.storage.SimpleChatStorage;
import me.theseems.tomshel.storage.SimplePunishmentStorage;
import me.theseems.tomshel.util.StringUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TomasBot extends TelegramLongPollingBot {
  private final CommandContainer commandContainer;
  private final PunishmentStorage punishmentStorage;
  private final ChatStorage chatStorage;

  public TomasBot() {
    commandContainer = new SimpleCommandContainer();
    punishmentStorage = new SimplePunishmentStorage();
    chatStorage = new SimpleChatStorage();
  }

  public TomasBot(ChatStorage storage) {
    commandContainer = new SimpleCommandContainer();
    punishmentStorage = new SimplePunishmentStorage();
    this.chatStorage = storage;
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

  public void sendBack(Update update, SendMessage message) {
    try {
      message.setChatId(update.getMessage().getChatId());
      execute(message);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  public void onUpdateReceived(Update update) {
    Message message = update.getMessage();
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

    if (punishmentStorage
        .getActivePunishment(update.getMessage().getFrom().getId(), PunishmentType.MUTED)
        .isPresent() || (chatStorage.isNoStickerMode() && update.getMessage().hasSticker())) {
      try {
        execute(
            new DeleteMessage()
                .setMessageId(update.getMessage().getMessageId())
                .setChatId(update.getMessage().getChatId()));
        return;
      } catch (TelegramApiException e) {
        e.printStackTrace();
      }
    }

    if (!message.hasText()) return;
    if (!message.getText().startsWith("/")) return;

    String text = message.getText();

    String[] args = text.split("\\ ");
    String label = args[0].substring(1);
    if (label.endsWith("@tomshel_bot")) {
      label = label.substring(0, Math.max(1, label.length() - 12));
    }

    String finalLabel = label;
    commandContainer
        .get(label)
        .ifPresent(
            command -> {
              if (commandContainer.isAccessible(
                  finalLabel, message.getChatId(), message.getFrom().getId())) {

                command.handle(this, StringUtils.skipOne(args), update);
              } else {
                sendBack(
                    update,
                    new SendMessage()
                        .setText("К сожалению, вы не можете использовать эту комманду!"));
              }
            });
  }

  @Override
  public void onClosing() {
    Main.save();
  }

  public String getBotUsername() {
    return "tomshel_bot";
  }

  public String getBotToken() {
    return "1322156348:AAFnwWsUneZWmlu-W_oP2MikvntcP56hGmc";
  }
}
