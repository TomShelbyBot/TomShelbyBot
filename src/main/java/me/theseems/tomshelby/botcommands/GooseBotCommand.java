package me.theseems.tomshelby.botcommands;

import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.command.SimpleBotCommand;
import me.theseems.tomshelby.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class GooseBotCommand extends SimpleBotCommand {
  public GooseBotCommand() {
    super(SimpleCommandMeta.onLabel("goose").description("Закинуть гуся."));
  }

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    bot.sendBack(
        update,
        new SendMessage()
            .setText(
                "ЗАПУСКАЕМ\n"
                    + "░ГУСЯ░▄▀▀▀▄░РАБОТЯГИ░░\n"
                    + "▄███▀░◐░░░▌░░░░░░░\n"
                    + "░░░░▌░░░░░▐░░░░░░░\n"
                    + "░░░░▐░░░░░▐░░░░░░░\n"
                    + "░░░░▌░░░░░▐▄▄░░░░░\n"
                    + "░░░░▌░░░░▄▀▒▒▀▀▀▀▄\n"
                    + "░░░▐░░░░▐▒▒▒▒▒▒▒▒▀▀▄\n"
                    + "░░░▐░░░░▐▄▒▒▒▒▒▒▒▒▒▒▀▄\n"
                    + "░░░░▀▄░░░░▀▄▒▒▒▒▒▒▒▒▒▒▀▄\n"
                    + "░░░░░░▀▄▄▄▄▄█▄▄▄▄▄▄▄▄▄▄▄▀▄\n"
                    + "░░░░░░░░░░░▌▌░▌▌░░░░░\n"
                    + "░░░░░░░░░░░▌▌░▌▌░░░░░\n"
                    + "░░░░░░░░░▄▄▌▌▄▌▌░░░░░"));
  }
}
