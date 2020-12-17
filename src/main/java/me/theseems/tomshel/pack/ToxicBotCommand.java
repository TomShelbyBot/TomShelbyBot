package me.theseems.tomshel.pack;

import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.command.SimpleBotCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ToxicBotCommand extends SimpleBotCommand {
  public ToxicBotCommand() {
    super(new SimpleCommandMeta().label("toxic").description("Поставить алерт на токсика"));
  }

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    if (!update.getMessage().isReply()) return;
    Message reply = update.getMessage().getReplyToMessage();
    bot.sendBack(update, new SendMessage().setText("ᅠ\n☣ ТОКСИК АЛЕРТ!! ☣\nА вы знали, что этот пользователь токсик?ᅠ\nᅠ\nБерегитесь!ᅠ").setReplyToMessageId(reply.getMessageId()));
  }
}
