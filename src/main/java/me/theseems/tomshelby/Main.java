package me.theseems.tomshelby;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.theseems.tomshelby.bootstrap.BootstrapManager;
import me.theseems.tomshelby.bootstrap.builtin.*;
import me.theseems.tomshelby.callback.CallbackManager;
import me.theseems.tomshelby.command.CommandContainer;
import me.theseems.tomshelby.config.BotConfig;
import me.theseems.tomshelby.pack.BotPackageManager;
import me.theseems.tomshelby.poll.PollManager;
import me.theseems.tomshelby.punishment.PunishmentHandler;
import me.theseems.tomshelby.storage.ChatStorage;
import me.theseems.tomshelby.storage.PunishmentStorage;
import me.theseems.tomshelby.storage.SimpleTomMeta;
import me.theseems.tomshelby.storage.adapters.SimpleTomMetaAdapter;
import me.theseems.tomshelby.update.UpdateHandlerManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
  // Hardcoded version
  public static final String TOM_BOT_VERSION = "0.10D (Support for new ids)";

  // Bot object itself
  private static ThomasBot bot;

  // Gson to support simple meta serialization
  private static final Gson gson =
      new GsonBuilder()
          .setPrettyPrinting()
          .registerTypeAdapter(SimpleTomMeta.class, new SimpleTomMetaAdapter())
          .create();

  // Components
  private static BotPackageManager botPackageManager;
  private static CommandContainer commandContainer;
  private static ChatStorage chatStorage;
  private static PunishmentStorage punishmentStorage;
  private static CallbackManager callbackManager;
  private static UpdateHandlerManager updateHandlerManager;
  private static PollManager pollManager;
  private static BotConfig botConfig;
  private static PunishmentHandler punishmentHandler;

  public static void save() {
    try {
      FileWriter writer = new FileWriter(new File(getBaseDir(), "chats.json"));
      new Gson()
          .newBuilder()
          .registerTypeAdapter(SimpleTomMeta.class, new SimpleTomMetaAdapter())
          .setPrettyPrinting()
          .create()
          .toJson(bot.getChatStorage(), writer);
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws Exception {
    PollTargetBootstrap pollTargetBootstrap = new PollTargetBootstrap();
    // Bootstrap manager (helps to mess with stuff on init)
    BootstrapManager bootstrapManager = new BootstrapManager();

    // Logger for bootstrap
    Logger logger = LogManager.getLogger("Bootstrap");

    // Init bootstraps
    bootstrapManager
        .init(new ConsoleInformationBootstrap())
        .init(new ConfigBootstrap())
        .init(new LoadPackagesBootstrap())
        .init(new ChatBootstrap())
        .init(pollTargetBootstrap)
        .init(new DefaultManagersBootstrap())
        .init(new InitBotBootstrap())
        .invokeInit(logger);

    // Target bootstraps (when bot is initialized)
    bootstrapManager
        .target(new AttachHandlersBootstrap())
        .target(new AttachCommandsBootstrap())
        .target(new EnablePackagesBootstrap())
        .target(pollTargetBootstrap)
        .target(new TelegramListenBootstrap())
        .invokeTarget(logger, bot);
  }

  public static ThomasBot getBot() {
    return bot;
  }

  public static File getBaseDir() {
    return new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath())
        .getParentFile();
  }

  public static Gson getGson() {
    return gson;
  }

  public static void setBotPackageManager(BotPackageManager botPackageManager) {
    Main.botPackageManager = botPackageManager;
  }

  public static void setCommandContainer(CommandContainer commandContainer) {
    Main.commandContainer = commandContainer;
  }

  public static void setChatStorage(ChatStorage chatStorage) {
    Main.chatStorage = chatStorage;
  }

  public static void setPunishmentStorage(PunishmentStorage punishmentStorage) {
    Main.punishmentStorage = punishmentStorage;
  }

  public static void setCallbackManager(CallbackManager callbackManager) {
    Main.callbackManager = callbackManager;
  }

  public static void setUpdateHandlerManager(UpdateHandlerManager updateHandlerManager) {
    Main.updateHandlerManager = updateHandlerManager;
  }

  public static void setPollManager(PollManager pollManager) {
    Main.pollManager = pollManager;
  }

  public static void setBotConfig(BotConfig botConfig) {
    Main.botConfig = botConfig;
  }

  public static void setPunishmentHandler(PunishmentHandler punishmentHandler) {
    Main.punishmentHandler = punishmentHandler;
  }

  public static BotPackageManager getBotPackageManager() {
    return botPackageManager;
  }

  public static CommandContainer getCommandContainer() {
    return commandContainer;
  }

  public static ChatStorage getChatStorage() {
    return chatStorage;
  }

  public static PunishmentStorage getPunishmentStorage() {
    return punishmentStorage;
  }

  public static CallbackManager getCallbackManager() {
    return callbackManager;
  }

  public static UpdateHandlerManager getUpdateHandlerManager() {
    return updateHandlerManager;
  }

  public static PollManager getPollManager() {
    return pollManager;
  }

  public static BotConfig getBotConfig() {
    return botConfig;
  }

  public static PunishmentHandler getPunishmentHandler() {
    return punishmentHandler;
  }

  public static void setBot(ThomasBot bot) {
    Main.bot = bot;
  }
}
