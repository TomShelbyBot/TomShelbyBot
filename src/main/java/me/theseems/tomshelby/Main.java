package me.theseems.tomshelby;

import com.google.gson.Gson;
import me.theseems.tomshelby.botcommands.*;
import me.theseems.tomshelby.botcommands.dev.*;
import me.theseems.tomshelby.callback.SimpleCallbackManager;
import me.theseems.tomshelby.command.SimpleCommandContainer;
import me.theseems.tomshelby.config.BotConfig;
import me.theseems.tomshelby.pack.JarBotPackageManager;
import me.theseems.tomshelby.pack.JavaBotPackage;
import me.theseems.tomshelby.punishment.DeleteMessageProcessor;
import me.theseems.tomshelby.punishment.MumbleMessageProcessor;
import me.theseems.tomshelby.punishment.SimplePunishmentHandler;
import me.theseems.tomshelby.storage.ChatStorage;
import me.theseems.tomshelby.storage.SimplePunishmentStorage;
import me.theseems.tomshelby.storage.simple.SimpleChatStorage;
import me.theseems.tomshelby.update.SimpleUpdateHandler;
import me.theseems.tomshelby.update.SimpleUpdateHandlerManager;
import me.theseems.tomshelby.update.handlers.*;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;

public class Main {
  public static final String TOM_BOT_VERSION = "0.5D (Package experimental)";

  private static ThomasBot bot;
  private static JarBotPackageManager packageManager;
  private static final File baseDir =
      new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath())
          .getParentFile();

  private static final File chatsFile = new File(baseDir, "chats.json");
  private static final File configFile = new File(baseDir, "config.json");
  private static final File pollsFile = new File(baseDir, "polls.json");

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

  private static ChatStorage loadChats() {
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

    return chatStorage;
  }

  private static JarBotPackageManager loadPackages() {
    packageManager = new JarBotPackageManager();
    File packageDir = new File(baseDir, "packs");
    if (!packageDir.exists()) {
      packageDir.mkdir();
    }

    try {
      packageManager.loadPackages(packageDir);
    } catch (IOException e) {
      System.err.println("Error loading plugins: " + e.getMessage());
      e.printStackTrace();
    }

    return packageManager;
  }

  private static void loadBot() {
    BotConfig config = loadConfig();
    if (config == null) {
      throw new IllegalStateException("Cannot boot without config.json with bot credentials!");
    }

    bot =
        new ThomasBot(
            new SimpleCommandContainer(),
            new SimplePunishmentStorage(),
            loadChats(),
            new SimplePunishmentHandler(),
            new SimpleCallbackManager(),
            new SimpleUpdateHandlerManager(),
            loadPackages(),
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
        .attach(new IdBotCommand())
        .attach(new BombBotCommand());

    // Development pack
    bot.getCommandContainer()
        .attach(new MetaGetBotCommand())
        .attach(new MetaPutBotCommand())
        .attach(new MetaDelBotCommand())
        .attach(new MetaMapBotCommand())
        .attach(new SaveAllBotCommand())
        .attach(new FatherExportBotCommand())
        .attach(new ListPackagesBotCommand());

    bot.getPunishmentHandler().add(new DeleteMessageProcessor());
    bot.getPunishmentHandler().add(new MumbleMessageProcessor());

    SimpleUpdateHandler.putConsecutively(
        bot,
        new InlineQueryHandler(),
        new CallbackQueryHandler(),
        new PunishmentHandler(),
        PollAnswerHandler.loadFrom(pollsFile),
        new WelcomeHandler(),
        new NonStickerModeHandler(),
        new CommandHandler());

    for (JavaBotPackage aPackage : bot.getPackageManager().getPackages()) {
      System.out.println("Enabling botpackage '" + aPackage.getInfo().getName() + "'");
      packageManager.enablePackage(bot, aPackage.getInfo().getName());
    }

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
