package me.theseems.tomshelby.handlers;

import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.update.SimpleUpdateHandler;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class InlineQueryHandler extends SimpleUpdateHandler {
  @Override
  public boolean handleUpdate(ThomasBot bot, Update update) {
    if (!update.hasInlineQuery()) return true;

    String query = update.getInlineQuery().getQuery();
    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < query.length(); i++) {
      if (i % 2 == 0) builder.append(Character.toUpperCase(query.charAt(i)));
      else builder.append(Character.toLowerCase(query.charAt(i)));
    }

    if (builder.length() == 0) return true;

    try {
      bot.execute(
          new AnswerInlineQuery()
              .setResults(
                  new InlineQueryResultArticle()
                      .setId("1")
                      .setTitle("ДаУнКеЙс")
                      .setDescription(builder.toString())
                      .setInputMessageContent(
                          new InputTextMessageContent().setMessageText(builder.toString())))
              .setInlineQueryId(update.getInlineQuery().getId()));
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }

    return false;
  }
}
