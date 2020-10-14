package me.theseems.tomshel.storage;

import me.theseems.tomshel.Main;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class SimpleChatStorage implements ChatStorage {
  private Map<Long, Map<String, Integer>> chatMap;
  private boolean stickerMode;

  public SimpleChatStorage() {
    chatMap = new HashMap<>();
  }

  /**
   * Lookup for user id
   *
   * @param chatId to lookup in
   * @param username to lookup for
   * @return user id
   */
  @Override
  public Optional<Integer> lookup(Long chatId, String username) {
    if (!chatMap.containsKey(chatId)) return Optional.empty();
    if (!chatMap.get(chatId).containsKey(username)) return Optional.empty();

    return Optional.ofNullable(chatMap.get(chatId).get(username));
  }

  @Override
  public boolean isNoStickerMode() {
    return stickerMode;
  }

  @Override
  public void setNoStickerMode(boolean stickerMode) {
    this.stickerMode = stickerMode;
  }

  @Override
  public Optional<ChatMember> lookupMember(Long chatId, String username) {
    if (!chatMap.containsKey(chatId)) return Optional.empty();
    if (!chatMap.get(chatId).containsKey(username)) return Optional.empty();

    int userId = chatMap.get(chatId).get(username);
    try {
      return Optional.of(
          Main.getBot().execute(new GetChatMember().setChatId(chatId).setUserId(userId)));
    } catch (TelegramApiException e) {
      return Optional.empty();
    }
  }

  /**
   * Save
   *
   * @param chatId
   * @param username
   * @param userId
   */
  @Override
  public void put(Long chatId, String username, Integer userId) {
    if (!chatMap.containsKey(chatId)) chatMap.put(chatId, new HashMap<>());
    chatMap.get(chatId).put(username, userId);
  }

  @Override
  public Collection<String> getResolvableUsernames(Long chatId) {
    if (!chatMap.containsKey(chatId)) return Collections.emptyList();
    return chatMap.get(chatId).keySet();
  }
}
