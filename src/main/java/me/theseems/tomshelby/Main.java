package me.theseems.tomshelby;

import com.google.gson.Gson;
import me.theseems.tomshelby.callback.SimpleCallbackManager;
import me.theseems.tomshelby.command.SimpleCommandContainer;
import me.theseems.tomshelby.command.builtin.HelpBotCommand;
import me.theseems.tomshelby.command.builtin.IdBotCommand;
import me.theseems.tomshelby.command.builtin.InfoBotCommand;
import me.theseems.tomshelby.config.AccessConfig;
import me.theseems.tomshelby.config.BotConfig;
import me.theseems.tomshelby.pack.BotPackage;
import me.theseems.tomshelby.pack.BotPackageInfo;
import me.theseems.tomshelby.pack.JarBotPackageManager;
import me.theseems.tomshelby.poll.MetaPollContainer;
import me.theseems.tomshelby.poll.MetaPollManager;
import me.theseems.tomshelby.punishment.SimplePunishmentHandler;
import me.theseems.tomshelby.storage.ChatStorage;
import me.theseems.tomshelby.storage.SimplePunishmentStorage;
import me.theseems.tomshelby.storage.SimpleTomMeta;
import me.theseems.tomshelby.storage.adapters.SimpleTomMetaAdapter;
import me.theseems.tomshelby.storage.impl.SimpleChatStorage;
import me.theseems.tomshelby.update.SimpleUpdateHandler;
import me.theseems.tomshelby.update.SimpleUpdateHandlerManager;
import me.theseems.tomshelby.update.builtin.*;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.util.stream.Collectors;

public class Main {
  // Hardcoded version
  public static final String TOM_BOT_VERSION = "0.9D (Polls, API improvements)";

  // Files
  private static final File baseDir =
      new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath())
          .getParentFile();
  private static final File chatsFile = new File(baseDir, "chats.json");
  private static final File configFile = new File(baseDir, "config.json");

  // Main objects
  private static ThomasBot bot;
  private static JarBotPackageManager packageManager;
  private static final Gson gson =
      new Gson()
          .newBuilder()
          .setPrettyPrinting()
          .registerTypeAdapter(SimpleTomMeta.class, new SimpleTomMetaAdapter())
          .create();

  public static void save() {
    System.out.println("Saving data to disk...");
    try {
      FileWriter writer = new FileWriter(chatsFile);
      gson.toJson(bot.getChatStorage(), writer);
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static BotConfig loadConfig() {
    try {
      return new Gson().fromJson(new FileReader(configFile), BotConfig.class);
    } catch (FileNotFoundException ignored) {}
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
      chatStorage = gson.fromJson(new FileReader(chatsFile), SimpleChatStorage.class);
      if (chatStorage == null) {
        System.err.println("Error loading storage! There's no one there.");
        System.out.println("Creating empty storage");
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
      BotConfig empty = new BotConfig(new AccessConfig("YOUR_BOT_USERNAME", "YOUR_BOT_TOKEN_FROM_BOTFATHER"));
      try {
        FileWriter writer = new FileWriter(configFile);
        gson.toJson(empty, writer);
        writer.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }

      System.err.println("Please, fill in the config.json in your bot's root dir");
      System.err.println("Bot can't start without it onboard");
      System.exit(1);
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
    MetaPollContainer metaPollContainer = new MetaPollContainer();
    bot =
        new ThomasBot(
            new SimpleCommandContainer(),
            new SimplePunishmentStorage(),
            chatStorage,
            new SimplePunishmentHandler(),
            new SimpleCallbackManager(),
            new SimpleUpdateHandlerManager(),
            jarBotPackageManager,
            new MetaPollManager(metaPollContainer),
            config);

    metaPollContainer.setThomasBot(bot);
    try {
      metaPollContainer.setSelfChatId(bot.getMe().getId());
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }

    // Builtin handlers
    SimpleUpdateHandler.putConsecutively(
        bot,
        new CallbackQueryHandler(),
        new InlineQueryHandler(),
        new PunishmentHandler(),
        new PollHandler(),
        new CommandHandler());

    // Builtin commands
    getBot()
        .getCommandContainer()
        .attach(new HelpBotCommand())
        .attach(new IdBotCommand())
        .attach(new InfoBotCommand());
  }

  private static void loadPacks() {
    for (BotPackageInfo pack :
        bot.getPackageManager()
            .getOrderManager()
            .order(
                bot.getPackageManager().getPackages().stream()
                    .map(BotPackage::getInfo)
                    .collect(Collectors.toList()))
            .getOrderedPackages()) {
      System.out.println("Enabling pack '" + pack.getName() + "' v" + pack.getVersion() + " by " + pack.getAuthor());
      packageManager.enablePackage(bot, pack.getName());
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
