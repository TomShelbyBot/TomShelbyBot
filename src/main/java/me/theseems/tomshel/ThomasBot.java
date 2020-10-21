package me.theseems.tomshel;

import me.theseems.tomshel.command.CommandContainer;
import me.theseems.tomshel.command.SimpleCommandContainer;
import me.theseems.tomshel.punishment.PunishmentHandler;
import me.theseems.tomshel.punishment.SimplePunishmentHandler;
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

public class ThomasBot extends TelegramLongPollingBot {
  private final CommandContainer commandContainer;
  private final PunishmentStorage punishmentStorage;
  private final ChatStorage chatStorage;
  private final PunishmentHandler punishmentHandler;

  public ThomasBot() {
    this(new SimpleChatStorage());
  }

  public ThomasBot(ChatStorage storage) {
    this.commandContainer = new SimpleCommandContainer();
    this.punishmentStorage = new SimplePunishmentStorage();
    this.chatStorage = storage;
    this.punishmentHandler = new SimplePunishmentHandler();
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

  public void sendBack(Update update, SendMessage message) {
    try {
      message.setChatId(update.getMessage().getChatId());
      execute(message);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  public void processUpdate(Update update) {
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

    if (chatStorage.isNoStickerMode() && update.getMessage().hasSticker()) {
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

    if (!punishmentHandler.handle(update)) return;
    if (!message.hasText()) return;
    if (!message.getText().startsWith("/")) return;

    String text = message.getText();

    String[] args = text.split(" ");
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

  public void onUpdateReceived(Update update) {
    try {
      processUpdate(update);
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getMessage());
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
        + '}';
  }
}
