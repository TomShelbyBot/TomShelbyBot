package me.theseems.tomshel.pack;

import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.command.SimpleCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.security.SecureRandom;

public class ThrowCoinCommand extends SimpleCommand {
  public ThrowCoinCommand() {
    super(
        new SimpleCommandMeta()
            .label("coin")
            .aliases("throw", "flip")
            .description("Подбросить монетку"));
  }

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    String text;
    if (new SecureRandom().nextBoolean()) {
      text = "\uD83D\uDFE1 Орел!";
    } else {
      text = "🟤 Решка!";
    }

    bot.sendBack(
        update,
        new SendMessage().setText(text).setReplyToMessageId(update.getMessage().getMessageId()));
  }
}
