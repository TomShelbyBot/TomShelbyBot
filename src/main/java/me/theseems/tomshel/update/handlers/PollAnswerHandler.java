package me.theseems.tomshel.update.handlers;

import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.storage.TomMeta;
import me.theseems.tomshel.update.SimpleUpdateHandler;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PollAnswerHandler extends SimpleUpdateHandler {
  private Map<String, Long> pollToChatMap;
  private File file;
  private static PollAnswerHandler instance;

  private PollAnswerHandler() {
    pollToChatMap = new HashMap<>();
  }

  private void save() {
    try {
      FileWriter writer = new FileWriter(file);
      new GsonBuilder().create().toJson(pollToChatMap, writer);
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static PollAnswerHandler getInstance() {
    return instance;
  }

  public static PollAnswerHandler loadFrom(File file) {
    if (instance != null) throw new IllegalStateException("Instance already exists");

    PollAnswerHandler handler = new PollAnswerHandler();
    handler.file = file;

    if (file.exists()) {
      try {
        //noinspection UnstableApiUsage
        handler.pollToChatMap =
            new GsonBuilder()
                .create()
                .fromJson(new FileReader(file), new TypeToken<Map<String, Long>>() {}.getType());
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }

    if (handler.pollToChatMap == null) {
      handler.pollToChatMap = new HashMap<>();
    }

    instance = handler;
    return handler;
  }

  public void addPoll(String pollId, Long chatId) {
    pollToChatMap.put(pollId, chatId);
    save();
  }

  public void removePoll(String pollId) {
    pollToChatMap.remove(pollId);
    save();
  }

  public boolean proxyHandleUpdate(ThomasBot bot, Update update) throws TelegramApiException {
    if (!update.hasPollAnswer()) return true;

    String pollId = update.getPollAnswer().getPollId();
    if (!pollToChatMap.containsKey(pollId)) return false;

    Long chatId = pollToChatMap.get(pollId);
    User user = update.getPollAnswer().getUser();
    String userName;

    if (user.getUserName() != null) {
      userName = user.getUserName();
    } else if (user.getFirstName() != null) {
      userName = user.getFirstName() + (user.getLastName() != null ? " " + user.getLastName() : "");
    } else {
      userName = "?<" + user.getId() + ">";
    }

    TomMeta meta = bot.getChatStorage().getChatMeta(chatId);
    String positiveReaction = meta.getString("pollPositive").orElse("\uD83D\uDE18");
    String negativeReaction = meta.getString("pollNegative").orElse("\uD83D\uDE1E");
    String rudeReaction = meta.getString("pollRude").orElse("\uD83E\uDD2C");

    String text = "";
    if (update.getPollAnswer().getOptionIds().contains(0)) {
      text += positiveReaction;
    } else if (update.getPollAnswer().getOptionIds().contains(1)) {
      text += negativeReaction;
    } else if (update.getPollAnswer().getOptionIds().contains(2)) {
      text += rudeReaction;
    }

    if (!text.isEmpty())
      bot.execute(new SendMessage().setText(text + ", @" + userName).setChatId(chatId));
    return false;
  }

  @Override
  public boolean handleUpdate(ThomasBot bot, Update update) {
    try {
      return proxyHandleUpdate(bot, update);
    } catch (TelegramApiException e) {
      e.printStackTrace();
      return false;
    }
  }
}
