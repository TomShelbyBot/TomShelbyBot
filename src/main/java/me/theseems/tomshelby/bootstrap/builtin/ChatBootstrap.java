package me.theseems.tomshelby.bootstrap.builtin;

import me.theseems.tomshelby.Main;
import me.theseems.tomshelby.bootstrap.InitBootstrap;
import me.theseems.tomshelby.storage.ChatStorage;
import me.theseems.tomshelby.storage.impl.SimpleChatStorage;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ChatBootstrap implements InitBootstrap {
  private static File chatsFile;

  public ChatBootstrap() {
    chatsFile = new File(Main.getBaseDir(), "chats.json");
  }

  @Override
  public void apply(Logger logger) {
    if (!chatsFile.exists()) {
      try {
        chatsFile.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    ChatStorage chatStorage = new SimpleChatStorage();
    try {
      chatStorage = Main.getGson().fromJson(new FileReader(chatsFile), SimpleChatStorage.class);
      if (chatStorage == null) {
        logger.warn("Error loading storage! There's no one there.");
        logger.info("Creating empty storage");
        chatStorage = new SimpleChatStorage();
      } else {
        logger.info("Loaded storage with chat count: " + chatStorage.getChatIds().size());
      }
    } catch (FileNotFoundException e) {
      logger.warn("Booting without loading chats from disk");
      e.printStackTrace();
    }

    Main.setChatStorage(chatStorage);
  }
}
