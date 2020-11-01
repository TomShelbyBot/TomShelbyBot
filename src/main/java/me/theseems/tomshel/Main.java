package me.theseems.tomshel;

import com.google.gson.Gson;
import me.theseems.tomshel.pack.*;
import me.theseems.tomshel.punishment.DeleteMessageProcessor;
import me.theseems.tomshel.punishment.MumbleMessageProcessor;
import me.theseems.tomshel.storage.SimpleChatStorage;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;

public class Main {
  private static ThomasBot bot;
  private static final File baseDir =
      new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath())
          .getParentFile();
  private static final File chatsFile = new File(baseDir, "chats.json");

  public static final String TOM_BOT_VERSION = "0.1D";

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

  private static void loadBot() {
    if (!chatsFile.exists()) {
      try {
        boolean isCreated = chatsFile.createNewFile();
        if (!isCreated) {
          System.err.println("Cannot init chats.json, using getting through without it.");
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    try {
      SimpleChatStorage storage =
          new Gson().fromJson(new FileReader(chatsFile), SimpleChatStorage.class);
      if (storage == null) {
        System.err.println("Error loading storage! There's no one there.");
        storage = new SimpleChatStorage();
      }

      bot = new ThomasBot(storage);
      System.out.println("Loaded storage with entries: " + storage.getEntryCount());
    } catch (FileNotFoundException e) {
      System.err.println("Booting without loading chats from disk");
      e.printStackTrace();
      bot = new ThomasBot();
    }
  }

  public static void initialize() {
    ApiContextInitializer.init();

    loadBot();
    bot.getCommandContainer()
        .attach(new TestCommand())
        .attach(new GooseCommand())
        .attach(new WallCommand())
        .attach(new BeatCommand())
        .attach(new LookupCommand())
        .attach(new MuteCommand())
        .attach(new UnmuteCommand())
        .attach(new AllCommand())
        .attach(new NoStickerCommand())
        .attach(new GoCommand())
        .attach(new HelpCommand())
        .attach(new InfoCommand())
        .attach(new ClapMuteCommand())
        .attach(new CheckPunishmentsCommand())
        .attach(new ThrowCoinCommand())
        .attach(new RandomNumberCommand())
        .attach(new SummonCommand())
        .attach(new UnsummonCommand());

    bot.getPunishmentHandler().add(new DeleteMessageProcessor());
    bot.getPunishmentHandler().add(new MumbleMessageProcessor());

    TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
    try {
      telegramBotsApi.registerBot(bot);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    initialize();
  }

  public static ThomasBot getBot() {
    return bot;
  }
}
