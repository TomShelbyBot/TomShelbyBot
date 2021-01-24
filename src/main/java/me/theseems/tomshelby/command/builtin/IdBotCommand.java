package me.theseems.tomshelby.command.builtin;

import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.command.BotCommandInfo;
import me.theseems.tomshelby.command.SimpleBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@BotCommandInfo(label = "id", description = "Узнать айди чата")
public class IdBotCommand extends SimpleBotCommand {
  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    bot.replyBack(
        update,
        new SendMessage()
            .setText("Айди чата: `" + update.getMessage().getChatId() + "`")
            .enableMarkdown(true));
  }
}
