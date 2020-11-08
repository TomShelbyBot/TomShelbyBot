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
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
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
    if (update.hasInlineQuery()) {
      handleInline(update);
      return;
    }

    if (update.hasCallbackQuery()) {
      callbackManager.call(this, update);
      return;
    }

    Message message = update.getMessage();
    handleSpecialWord(update, message);
    if (!punishmentHandler.handle(update)) return;
    if (update.hasPollAnswer()) {
      handlePoll(update);
      return;
    }

    if (message == null) return;
    if (update.hasMessage() && message.getFrom().getUserName().equals(getBotUsername())) return;
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
    return "tomshel_bot";
    // return "tom_night_bot";
  }

  public String getBotToken() {
    return "1322156348:AAFnwWsUneZWmlu-W_oP2MikvntcP56hGmc";
    // return "1118855263:AAHy7xNR67KWYfLEjTzBQ5GgFIXl0GCUavs";
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
    if (message != null
        && message.getFrom().getId() == 311245296
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

  private void handleInline(Update update) {
    String query = update.getInlineQuery().getQuery();
    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < query.length(); i++) {
      if (i % 2 == 0) builder.append(Character.toUpperCase(query.charAt(i)));
      else builder.append(Character.toLowerCase(query.charAt(i)));
    }

    if (builder.length() == 0) return;

    try {
      execute(
          new AnswerInlineQuery()
              .setResults(
                  new InlineQueryResultArticle()
                      .setId("1")
                      .setTitle("ДаУнКеЙс")
                      .setDescription(builder.toString())
                      .setInputMessageContent(
                          new InputTextMessageContent().setMessageText(builder.toString())))
              .setInlineQueryId(update.getInlineQuery().getId()));
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  private void handlePoll(Update update) {
    try {
      String userName =
          update.getPollAnswer().getUser() != null
              ? update.getPollAnswer().getUser().getUserName()
              : "<anon>";
      if (userName == null)
        userName = "Неопознанный импостер <" + update.getPollAnswer().getUser().getId() + ">";

      if (update.getPollAnswer().getOptionIds().contains(0)) {
        execute(
            new SendMessage()
                .setText("\uD83D\uDE18 @" + userName)
                .setChatId(chatStorage.getChatIds().iterator().next()));
      } else if (update.getPollAnswer().getOptionIds().contains(1)) {
        execute(
            new SendMessage()
                .setText("\uD83D\uDE1E @" + userName)
                .setChatId(chatStorage.getChatIds().iterator().next()));
      } else if (update.getPollAnswer().getOptionIds().contains(2)) {
        execute(
            new SendMessage()
                .setText("+ ПАШЕЛ НАХУЙ КТО ПРОГОЛОСОВАЛ ЗА ПАШЕЛ НАХУЙ!! СУКА, @" + userName)
                .setChatId(chatStorage.getChatIds().iterator().next()));
      }
    } catch (Exception ignored) {
    }
  }

  private void handleWelcome(Update update, Message message) {
    if (message != null
        && !chatStorage.lookup(message.getChatId(), message.getFrom().getUserName()).isPresent()) {
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
