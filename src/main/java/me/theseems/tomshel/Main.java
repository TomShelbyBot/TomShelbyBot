package me.theseems.tomshel;

import com.google.gson.Gson;
import me.theseems.tomshel.callback.SimpleCallbackManager;
import me.theseems.tomshel.command.SimpleCommandContainer;
import me.theseems.tomshel.config.BotConfig;
import me.theseems.tomshel.pack.*;
import me.theseems.tomshel.punishment.DeleteMessageProcessor;
import me.theseems.tomshel.punishment.MumbleMessageProcessor;
import me.theseems.tomshel.punishment.SimplePunishmentHandler;
import me.theseems.tomshel.storage.ChatStorage;
import me.theseems.tomshel.storage.SimpleChatStorage;
import me.theseems.tomshel.storage.SimplePunishmentStorage;
import me.theseems.tomshel.update.SimpleUpdateHandler;
import me.theseems.tomshel.update.SimpleUpdateHandlerManager;
import me.theseems.tomshel.update.handlers.*;
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
  private static final File configFile = new File(baseDir, "config.json");

  public static final String TOM_BOT_VERSION = "0.2D";

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

  private static BotConfig loadConfig() {
    try {
      return new Gson().fromJson(new FileReader(configFile), BotConfig.class);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return null;
  }

  private static void loadBot() {
    BotConfig config = loadConfig();
    if (config == null) {
      throw new IllegalStateException("Cannot boot without config.json with bot credentials!");
    }

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

    ChatStorage chatStorage = new SimpleChatStorage();
    try {
      chatStorage = new Gson().fromJson(new FileReader(chatsFile), SimpleChatStorage.class);

      if (chatStorage == null) {
        System.err.println("Error loading storage! There's no one there.");
        chatStorage = new SimpleChatStorage();
      } else {
        System.out.println("Loaded storage with chat count: " + chatStorage.getChatIds().size());
      }
    } catch (FileNotFoundException e) {
      System.err.println("Booting without loading chats from disk");
      e.printStackTrace();
    }

    bot =
        new ThomasBot(
            new SimpleCommandContainer(),
            new SimplePunishmentStorage(),
            chatStorage,
            new SimplePunishmentHandler(),
            new SimpleCallbackManager(),
            new SimpleUpdateHandlerManager(),
            config);
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
        .attach(new UnsummonCommand())
        .attach(new SayCommand())
        .attach(new FCommand())
        .attach(new ToxicCommand());

    bot.getPunishmentHandler().add(new DeleteMessageProcessor());
    bot.getPunishmentHandler().add(new MumbleMessageProcessor());

    SimpleUpdateHandler.putConsecutively(
        bot,
        new InlineQueryHandler(),
        new CallbackQueryHandler(),
        new SpecialWordHandler(),
        new PunishmentHandler(),
        new PollAnswerHandler(),
        new WelcomeHandler(),
        new NonStickerModeHandler(),
        new CommandHandler());

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
