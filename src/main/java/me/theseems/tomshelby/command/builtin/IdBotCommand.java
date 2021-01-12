package me.theseems.tomshelby.command.builtin;

import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.command.SimpleBotCommand;
import me.theseems.tomshelby.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class IdBotCommand extends SimpleBotCommand {
  public IdBotCommand() {
    super(SimpleCommandMeta.onLabel("id").description("Узнать id чата").alias("chatid"));
  }

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    bot.replyBack(
        update,
        new SendMessage()
            .setText("Айди чата: `" + update.getMessage().getChatId() + "`")
            .enableMarkdown(true));
  }
}
