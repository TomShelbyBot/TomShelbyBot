package me.theseems.tomshel.storage;

import org.telegram.telegrambots.meta.api.objects.ChatMember;

import java.util.Collection;
import java.util.Optional;

public interface ChatStorage {
  /**
   * Lookup for user id
   * @param chatId to lookup in
   * @param username to lookup for
   * @return user id
   */
  Optional<Integer> lookup(Long chatId, String username);

  boolean isNoStickerMode();

  void setNoStickerMode(boolean stickerMode);

  /**
   * Lookup for user id
   * @param chatId to lookup in
   * @param username to lookup for
   * @return user id
   */
  Optional<ChatMember> lookupMember(Long chatId, String username);

  /**
   * Save
   * @param chatId
   * @param username
   * @param userId
   */
  void put(Long chatId, String username, Integer userId);

  Collection<String> getResolvableUsernames(Long chatId);

  Collection<Long> getChatIds();
}
