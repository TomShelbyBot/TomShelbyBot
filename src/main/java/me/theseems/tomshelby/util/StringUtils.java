package me.theseems.tomshelby.util;

import me.theseems.tomshelby.Main;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class StringUtils {

    public static class DragResult {
        private final boolean skipArg;
        private final ChatMember member;

        public DragResult(boolean skipArg, ChatMember member) {
            this.skipArg = skipArg;
            this.member = member;
        }

        public Optional<ChatMember> getMember() {
            return Optional.ofNullable(member);
        }

        public boolean isSkipArg() {
            return skipArg;
        }
    }

    public static DragResult dragFrom(Update update, String[] args) {
        if (!update.hasMessage()) return new DragResult(false, null);

        Message message = update.getMessage();
        if (message.isReply()) {
            try {
                GetChatMember getChatMember = new GetChatMember();
                getChatMember.setChatId(String.valueOf(message.getChatId()));
                getChatMember.setUserId(message.getReplyToMessage().getFrom().getId());

                return new DragResult(false, Main.getBot().execute(getChatMember));
            } catch (TelegramApiException e) {
                e.printStackTrace();
                return new DragResult(false, null);
            }
        }

        if (args.length == 0) {
            return new DragResult(false, null);
        }

        String username = args[0];
        if (username.startsWith("@")) {
            username = username.substring(1);
        }

        Optional<ChatMember> chatMember =
                Main.getBot().getChatStorage().lookupMember(String.valueOf(message.getChatId()), username);
        return new DragResult(true, chatMember.orElse(null));
    }

    public static String[] skipOne(String[] original) {
        List<String> stringList = new ArrayList<>(Arrays.asList(original).subList(1, original.length));
        return stringList.toArray(new String[]{});
    }
}
