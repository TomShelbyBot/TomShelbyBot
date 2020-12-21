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

  /**
   * Lookup for user id
   * @param chatId to lookup in
   * @param username to lookup for
   * @return user id
   */
  Optional<ChatMember> lookupMember(Long chatId, String username);

  /**
   * Save nickname for user in specific chat
   * @param chatId to put in
   * @param username to put for
   * @param userId to put
   */
  void put(Long chatId, String username, Integer userId);

  /**
   * Get chat meta
   * @param chatId to get for
   * @return meta
   */
  TomMeta getChatMeta(Long chatId);

  /**
   * Get resolvable nicknames for chat
   * @param chatId to get nicknames for
   * @return nicknames
   */
  Collection<String> getResolvableUsernames(Long chatId);

  /**
   * Get chat ids there are
   * @return chat ids
   */
  Collection<Long> getChatIds();
}
