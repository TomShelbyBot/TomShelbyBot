package me.theseems.tomshelby.botcommands;

import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.command.SimpleBotCommand;
import me.theseems.tomshelby.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ToxicBotCommand extends SimpleBotCommand {
  public ToxicBotCommand() {
    super(new SimpleCommandMeta().label("toxic").description("Поставить алерт на токсика"));
  }

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    if (!update.getMessage().isReply()) {
      bot.replyBackText(update, "Укажите на токсика, ответив на его сообщение командой /toxic");
      return;
    }

    Message reply = update.getMessage().getReplyToMessage();
    bot.sendBack(
        update,
        new SendMessage()
            .setText(
                "ᅠ\n☣ ТОКСИК АЛЕРТ!! ☣\nА вы знали, что этот пользователь токсик?ᅠ\nᅠ\nБерегитесь!ᅠ")
            .setReplyToMessageId(reply.getMessageId()));
  }
}
