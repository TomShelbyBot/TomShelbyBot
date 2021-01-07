package me.theseems.tomshelby;

import com.google.gson.Gson;
import me.theseems.tomshelby.callback.SimpleCallbackManager;
import me.theseems.tomshelby.command.SimpleCommandContainer;
import me.theseems.tomshelby.command.builtin.HelpBotCommand;
import me.theseems.tomshelby.command.builtin.IdBotCommand;
import me.theseems.tomshelby.command.builtin.InfoBotCommand;
import me.theseems.tomshelby.config.BotConfig;
import me.theseems.tomshelby.pack.BotPackage;
import me.theseems.tomshelby.pack.JarBotPackageManager;
import me.theseems.tomshelby.punishment.SimplePunishmentHandler;
import me.theseems.tomshelby.storage.ChatStorage;
import me.theseems.tomshelby.storage.SimplePunishmentStorage;
import me.theseems.tomshelby.storage.simple.SimpleChatStorage;
import me.theseems.tomshelby.update.SimpleUpdateHandler;
import me.theseems.tomshelby.update.SimpleUpdateHandlerManager;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import me.theseems.tomshelby.update.builtin.*;

import java.io.*;

public class Main {
  public static final String TOM_BOT_VERSION = "0.6D (Meta extend)";

  private static ThomasBot bot;
  private static JarBotPackageManager packageManager;
  private static final File baseDir =
      new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath())
          .getParentFile();

  private static final File chatsFile = new File(baseDir, "chats.json");
  private static final File configFile = new File(baseDir, "config.json");

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
        chatsFile.createNewFile();
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

  private static JarBotPackageManager loadPackages() throws IOException {
    packageManager = new JarBotPackageManager();
    File packageDir = new File(baseDir, "packs");
    if (!packageDir.exists()) {
      packageDir.mkdir();
    }

    packageManager.loadPackages(packageDir);
    return packageManager;
  }

  private static void loadBot() {
    BotConfig config = loadConfig();
    if (config == null) {
      throw new IllegalStateException("Cannot boot without config.json with bot credentials!");
    }

    JarBotPackageManager jarBotPackageManager = new JarBotPackageManager();
    try {
      System.out.println("Loading packages...");
      jarBotPackageManager = loadPackages();
    } catch (IOException e) {
      System.err.println("Error occurred loading packages: " + e.getMessage());
      e.printStackTrace();
    }

    System.out.println("Loading chats...");
    ChatStorage chatStorage = loadChats();
    bot =
        new ThomasBot(
            new SimpleCommandContainer(),
            new SimplePunishmentStorage(),
            chatStorage,
            new SimplePunishmentHandler(),
            new SimpleCallbackManager(),
            new SimpleUpdateHandlerManager(),
            jarBotPackageManager,
            config);

    // Builtin handlers
    SimpleUpdateHandler.putConsecutively(
        bot,
        new CallbackQueryHandler(),
        new CallbackQueryHandler(),
        new PunishmentHandler(),
        new CommandHandler());

    // Builtin commands
    getBot()
        .getCommandContainer()
        .attach(new HelpBotCommand())
        .attach(new IdBotCommand())
        .attach(new InfoBotCommand());
  }

  private static void loadPacks() {
    for (BotPackage pack : bot.getPackageManager().getPackages()) {
      System.out.println(
          "Enabling pack '" + pack.getInfo().getName() + "' by " + pack.getInfo().getAuthor());
      packageManager.enablePackage(bot, pack.getInfo().getName());
    }
  }

  public static void initialize() {
    System.out.println("Initializing Telegram API communication...");
    ApiContextInitializer.init();

    System.out.println("Loading bot...");
    loadBot();

    System.out.println("Enabling packages...");
    loadPacks();

    TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
    try {
      telegramBotsApi.registerBot(bot);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  public static JarBotPackageManager getPackageManager() {
    return packageManager;
  }

  public static void main(String[] args) {
    initialize();
  }

  public static ThomasBot getBot() {
    return bot;
  }
}
