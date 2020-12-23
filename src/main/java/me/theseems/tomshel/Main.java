package me.theseems.tomshel;

import com.google.gson.Gson;
import me.theseems.tomshel.callback.SimpleCallbackManager;
import me.theseems.tomshel.command.SimpleCommandContainer;
import me.theseems.tomshel.config.BotConfig;
import me.theseems.tomshel.pack.*;
import me.theseems.tomshel.pack.dev.*;
import me.theseems.tomshel.punishment.DeleteMessageProcessor;
import me.theseems.tomshel.punishment.MumbleMessageProcessor;
import me.theseems.tomshel.punishment.SimplePunishmentHandler;
import me.theseems.tomshel.storage.ChatStorage;
import me.theseems.tomshel.storage.SimplePunishmentStorage;
import me.theseems.tomshel.storage.simple.SimpleChatStorage;
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
  private static final File pollsFile = new File(baseDir, "polls.json");

  public static final String TOM_BOT_VERSION = "0.4D";

  public static void save() {
    System.out.println("Saving to disk...");
    try {
      FileWriter writer = new FileWriter(chatsFile);
      new Gson().newBuilder().setPrettyPrinting().create().toJson(bot.getChatStorage(), writer);
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

    // Main pack
    bot.getCommandContainer()
        .attach(new GooseBotCommand())
        .attach(new WallBotCommand())
        .attach(new BeatBotCommand())
        .attach(new LookupBotCommand())
        .attach(new MuteBotCommand())
        .attach(new UnmuteBotCommand())
        .attach(new AllBotCommand())
        .attach(new NoStickerBotCommand())
        .attach(new GoBotCommand())
        .attach(new HelpBotCommand())
        .attach(new InfoBotCommand())
        .attach(new ClapMuteBotCommand())
        .attach(new CheckPunishmentsBotCommand())
        .attach(new ThrowCoinBotCommand())
        .attach(new RandomNumberBotCommand())
        .attach(new SummonBotCommand())
        .attach(new UnsummonBotCommand())
        .attach(new SayBotCommand())
        .attach(new RespectBotCommand())
        .attach(new ToxicBotCommand())
        .attach(new IdBotCommand());

    // Development pack
    bot.getCommandContainer()
        .attach(new MetaGetBotCommand())
        .attach(new MetaPutBotCommand())
        .attach(new MetaDelBotCommand())
        .attach(new MetaMapBotCommand())
        .attach(new SaveAllBotCommand());

    bot.getPunishmentHandler().add(new DeleteMessageProcessor());
    bot.getPunishmentHandler().add(new MumbleMessageProcessor());

    SimpleUpdateHandler.putConsecutively(
        bot,
        new InlineQueryHandler(),
        new CallbackQueryHandler(),
        new SpecialWordHandler(),
        new PunishmentHandler(),
        PollAnswerHandler.loadFrom(pollsFile),
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
