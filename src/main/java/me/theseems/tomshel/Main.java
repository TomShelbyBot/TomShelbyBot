package me.theseems.tomshel;

import com.google.gson.Gson;
import me.theseems.tomshel.pack.*;
import me.theseems.tomshel.storage.ChatStorage;
import me.theseems.tomshel.storage.SimpleChatStorage;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;

public class Main {
  private static TomasBot bot;
  private static final File baseDir =
      new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
  private static final File chatsFile = new File(baseDir, "chats.json");

  public static void save() {
    System.out.println("Saving to disk");
    try {
      FileWriter writer = new FileWriter(chatsFile);
      new Gson().toJson(bot.getChatStorage(), writer);
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void initBot() {
    if (!chatsFile.exists()) {
      try {
        chatsFile.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    try {
      ChatStorage storage = new Gson().fromJson(new FileReader(chatsFile), SimpleChatStorage.class);
      bot = new TomasBot(storage);
    } catch (FileNotFoundException e) {
      System.err.println("Booting without loading chats from disk");
      e.printStackTrace();
      bot = new TomasBot();
    }
  }

  public static void main(String[] args) {
    ApiContextInitializer.init();

    initBot();
    bot.getCommandContainer()
        .attach(new TestCommand())
        .attach(new GooseCommand())
        .attach(new WallCommand())
        .attach(new StraponCommand())
        .attach(new LookupCommand())
        .attach(new MuteCommand())
        .attach(new UnmuteCommand())
        .attach(new AtAllCommand())
        .attach(new SetTitleCommand())
        .attach(new NoStickerCommand())
        .attach(new GoWatchCommand());

    TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
    try {
      telegramBotsApi.registerBot(bot);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  public static TomasBot getBot() {
    return bot;
  }
}
