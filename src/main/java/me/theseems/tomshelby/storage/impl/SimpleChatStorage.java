package me.theseems.tomshelby.storage.impl;

import me.theseems.tomshelby.Main;
import me.theseems.tomshelby.storage.TomMeta;
import me.theseems.tomshelby.storage.ChatStorage;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class SimpleChatStorage implements ChatStorage {
  private final Map<Long, SimpleChatEntry> chatMap;

  public SimpleChatStorage() {
    chatMap = new HashMap<>();
  }

  @Override
  public Optional<Integer> lookup(Long chatId, String username) {
    if (!chatMap.containsKey(chatId)) return Optional.empty();
    if (!chatMap.get(chatId).containsUser(username)) return Optional.empty();

    return chatMap.get(chatId).getUserId(username);
  }

  @Override
  public Optional<ChatMember> lookupMember(Long chatId, String username) {
    if (!chatMap.containsKey(chatId)) return Optional.empty();

    Optional<Integer> userIdOptional = chatMap.get(chatId).getUserId(username);
    if (!userIdOptional.isPresent()) return Optional.empty();

    int userId = userIdOptional.get();
    try {
      return Optional.of(
          Main.getBot().execute(new GetChatMember().setChatId(chatId).setUserId(userId)));
    } catch (TelegramApiException e) {
      return Optional.empty();
    }
  }

  @Override
  public void put(Long chatId, String username, Integer userId) {
    if (!chatMap.containsKey(chatId)) chatMap.put(chatId, new SimpleChatEntry());
    chatMap.get(chatId).putUser(username, userId);
  }

  @Override
  public TomMeta getChatMeta(Long chatId) {
    if (!chatMap.containsKey(chatId)) {
      chatMap.put(chatId, new SimpleChatEntry());
    }

    return chatMap.get(chatId).getMeta();
  }

  @Override
  public Collection<String> getResolvableUsernames(Long chatId) {
    if (!chatMap.containsKey(chatId)) return Collections.emptyList();
    return chatMap.get(chatId).getUserNicknames();
  }

  @Override
  public Collection<Long> getChatIds() {
    return chatMap.keySet();
  }
}
