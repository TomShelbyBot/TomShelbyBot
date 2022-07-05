package me.theseems.tomshelby.storage.impl;

import me.theseems.tomshelby.Main;
import me.theseems.tomshelby.storage.TomMeta;
import me.theseems.tomshelby.storage.ChatStorage;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class SimpleChatStorage implements ChatStorage {
    private final Map<String, SimpleChatEntry> chatMap;

    public SimpleChatStorage() {
        chatMap = new HashMap<>();
    }

    @Override
    public Optional<Long> lookup(String chatId, String username) {
        if (!chatMap.containsKey(chatId)) return Optional.empty();
        if (!chatMap.get(chatId).containsUser(username)) return Optional.empty();

        return chatMap.get(chatId).getUserId(username);
    }

    @Override
    public Optional<ChatMember> lookupMember(String chatId, String username) {
        if (!chatMap.containsKey(chatId)) return Optional.empty();

        Optional<Long> userIdOptional = chatMap.get(chatId).getUserId(username);
        if (!userIdOptional.isPresent()) return Optional.empty();

        long userId = userIdOptional.get();
        try {
            GetChatMember getChatMember = new GetChatMember();
            getChatMember.setChatId(chatId);
            getChatMember.setUserId(userId);

            return Optional.of(Main.getBot().execute(getChatMember));
        } catch (TelegramApiException e) {
            return Optional.empty();
        }
    }

    @Override
    public void put(String chatId, String username, Long userId) {
        if (!chatMap.containsKey(chatId)) {
            chatMap.put(chatId, new SimpleChatEntry());
        }

        chatMap.get(chatId).putUser(username, userId);
    }

    @Override
    public TomMeta getChatMeta(String chatId) {
        if (!chatMap.containsKey(chatId)) {
            chatMap.put(chatId, new SimpleChatEntry());
        }

        return chatMap.get(chatId).getMeta();
    }

    @Override
    public Collection<String> getResolvableUsernames(String chatId) {
        if (!chatMap.containsKey(chatId)) {
            return Collections.emptyList();
        }

        return chatMap.get(chatId).getUserNicknames();
    }

    @Override
    public Collection<String> getChatIds() {
        return chatMap.keySet();
    }
}
