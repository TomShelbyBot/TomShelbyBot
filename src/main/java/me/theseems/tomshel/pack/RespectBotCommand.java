package me.theseems.tomshel.pack;

import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.command.SimpleBotCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.objects.Update;

public class RespectBotCommand extends SimpleBotCommand {
  public RespectBotCommand() {
    super(new SimpleCommandMeta().label("f").description("F in chat"));
  }

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    bot.getCommandContainer()
        .get("summon")
        .ifPresent(command -> command.handle(bot, new String[] {"/say f"}, update));
    bot.replyBackText(update, "Закиньте F в чатик пацаны");
  }
}
