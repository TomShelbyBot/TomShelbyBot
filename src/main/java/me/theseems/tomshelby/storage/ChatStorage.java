package me.theseems.tomshelby.storage;

import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

import java.util.Collection;
import java.util.Optional;

public interface ChatStorage {
  /**
   * Lookup for user id
   *
   * @param chatId to lookup in
   * @param username to lookup for
   * @return user id
   */
  Optional<Long> lookup(String chatId, String username);

  /**
   * Lookup for user id
   *
   * @param chatId to lookup in
   * @param username to lookup for
   * @return user id
   */
  Optional<ChatMember> lookupMember(String chatId, String username);

  /**
   * Save nickname for user in specific chat
   *
   * @param chatId to put in
   * @param username to put for
   * @param userId to put
   */
  void put(String chatId, String username, Long userId);

  /**
   * Get chat meta
   *
   * @param chatId to get for
   * @return meta
   */
  TomMeta getChatMeta(String chatId);

  /**
   * Get resolvable nicknames for chat
   *
   * @param chatId to get nicknames for
   * @return nicknames
   */
  Collection<String> getResolvableUsernames(String chatId);

  /**
   * Get chat ids there are
   *
   * @return chat ids
   */
  Collection<String> getChatIds();
}
