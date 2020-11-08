package me.theseems.tomshel.pack;

import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.command.SimpleCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Timer;
import java.util.TimerTask;

public class FCommand extends SimpleCommand {
  public FCommand() {
    super(new SimpleCommandMeta().label("f").description("F in chat"));
  }

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    bot.getCommandContainer()
        .get("summon")
        .ifPresent(command -> command.handle(bot, new String[] {"/say f"}, update));
    bot.sendBack(
        update,
        new SendMessage()
            .setText("Закиньте F в чатик пацаны")
            .setReplyToMessageId(update.getMessage().getMessageId()));
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        bot.getCommandContainer()
            .get("unsummon")
            .ifPresent(command -> command.handle(bot, new String[] {}, update));
      }
    }, 10000);
  }
}
