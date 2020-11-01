package me.theseems.tomshel.pack;

import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.command.SimpleCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

public class UnsummonCommand extends SimpleCommand {

  public UnsummonCommand() {
    super(
        new SimpleCommandMeta().label("unsummon").description("Сбросить призыв прописать комманду"));
  }

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    bot.sendBack(update, new SendMessage().setText("Сброшено").setReplyMarkup(new ReplyKeyboardRemove()));
  }
}
