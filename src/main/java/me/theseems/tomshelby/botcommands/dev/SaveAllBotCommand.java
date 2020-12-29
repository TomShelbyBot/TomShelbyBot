package me.theseems.tomshelby.botcommands.dev;

import me.theseems.tomshelby.Main;
import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.command.CommandMeta;
import me.theseems.tomshelby.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SaveAllBotCommand implements DevPermissibleBotCommand {
  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    Main.save();
    bot.sendBack(
        update,
        new SendMessage().setText("OK").setReplyToMessageId(update.getMessage().getMessageId()));
  }

  @Override
  public CommandMeta getMeta() {
    return SimpleCommandMeta.onLabel("saveall");
  }
}
