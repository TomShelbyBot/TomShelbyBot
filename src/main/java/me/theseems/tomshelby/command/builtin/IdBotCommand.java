package me.theseems.tomshelby.command.builtin;

import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.command.Command;
import me.theseems.tomshelby.command.SimpleBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Command(label = "id", description = "Узнать айди чата")
public class IdBotCommand extends SimpleBotCommand {
  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setText("Айди чата: `" + update.getMessage().getChatId() + "`");
    sendMessage.enableMarkdown(true);

    bot.replyBack(update, sendMessage);
  }
}
