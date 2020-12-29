package me.theseems.tomshelby.botcommands;

import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.command.SimpleBotCommand;
import me.theseems.tomshelby.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.security.SecureRandom;

public class ThrowCoinBotCommand extends SimpleBotCommand {
  public ThrowCoinBotCommand() {
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

    bot.replyBackText(update, text);
  }
}
