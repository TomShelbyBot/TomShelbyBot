package me.theseems.tomshel.pack.dev;

import me.theseems.tomshel.Main;
import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.command.CommandMeta;
import me.theseems.tomshel.command.SimpleCommandMeta;
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
