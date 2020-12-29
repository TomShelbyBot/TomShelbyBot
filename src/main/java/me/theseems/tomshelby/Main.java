package me.theseems.tomshelby;

import com.google.gson.Gson;
import me.theseems.tomshelby.botcommands.dev.meta.*;
import me.theseems.tomshelby.botcommands.dev.misc.FatherExportBotCommand;
import me.theseems.tomshelby.botcommands.dev.pack.DisablePackBotCommand;
import me.theseems.tomshelby.botcommands.dev.pack.EnablePackBotCommand;
import me.theseems.tomshelby.botcommands.dev.pack.ListPackBotCommand;
import me.theseems.tomshelby.callback.SimpleCallbackManager;
import me.theseems.tomshelby.command.SimpleCommandContainer;
import me.theseems.tomshelby.config.BotConfig;
import me.theseems.tomshelby.handlers.CallbackQueryHandler;
import me.theseems.tomshelby.handlers.CommandHandler;
import me.theseems.tomshelby.handlers.InlineQueryHandler;
import me.theseems.tomshelby.handlers.PunishmentHandler;
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

    // Development pack
    bot.getCommandContainer()
        .attach(new MetaGetBotCommand())
        .attach(new MetaPutBotCommand())
        .attach(new MetaDelBotCommand())
        .attach(new MetaMapBotCommand())
        .attach(new SaveAllBotCommand())
        .attach(new FatherExportBotCommand())
        .attach(new EnablePackBotCommand())
        .attach(new DisablePackBotCommand())
        .attach(new ListPackBotCommand());

    SimpleUpdateHandler.putConsecutively(bot, new InlineQueryHandler(), new CallbackQueryHandler(), new PunishmentHandler());
    CommandHandler handler = new CommandHandler();
    handler.setPriority(1000);
    getBot().getUpdateHandlerManager().addUpdateHandler(handler);

    for (BotPackage pack : bot.getPackageManager().getPackages()) {
      System.out.println("Enabling pack '" + pack.getInfo().getName() + "'");
      packageManager.enablePackage(bot, pack.getInfo().getName());
    }

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
