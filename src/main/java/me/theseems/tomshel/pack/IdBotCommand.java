package me.theseems.tomshel.pack;

import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.command.SimpleBotCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class IdBotCommand extends SimpleBotCommand {
  public IdBotCommand() {
    super(SimpleCommandMeta.onLabel("id").description("Узнать id чата").alias("chatid"));
  }

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    bot.sendBack(
        update,
        new SendMessage()
            .setText("Айди чата: `" + update.getMessage().getChatId() + "`")
            .enableMarkdown(true)
            .setReplyToMessageId(update.getMessage().getMessageId()));
  }
}
