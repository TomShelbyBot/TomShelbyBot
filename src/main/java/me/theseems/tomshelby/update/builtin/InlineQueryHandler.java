package me.theseems.tomshelby.update.builtin;

import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.update.SimpleUpdateHandler;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;

public class InlineQueryHandler extends SimpleUpdateHandler {
    @Override
    public boolean handleUpdate(ThomasBot bot, Update update) {
        if (!update.hasInlineQuery()) return true;

        String query = update.getInlineQuery().getQuery();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < query.length(); i++) {
            char originalChar = query.charAt(i);
            char morphedChar =
                    i % 2 == 0 ? Character.toUpperCase(originalChar) : Character.toLowerCase(originalChar);

            builder.append(morphedChar);
        }

        if (builder.length() == 0) {
            return true;
        }

        try {
            InputTextMessageContent content = new InputTextMessageContent();
            content.setMessageText(builder.toString());

            InlineQueryResultArticle article = new InlineQueryResultArticle();
            article.setId("1");
            article.setTitle("ДаУнКеЙс");
            article.setDescription(builder.toString());
            article.setInputMessageContent(content);

            AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery();
            answerInlineQuery.setResults(Collections.singletonList(article));
            answerInlineQuery.setInlineQueryId(update.getInlineQuery().getId());

            bot.execute(answerInlineQuery);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        return false;
    }
}
