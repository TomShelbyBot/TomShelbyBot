package me.theseems.tomshel.pack;

import me.theseems.tomshel.TomasBot;
import me.theseems.tomshel.command.OnlyAdminRestricted;
import me.theseems.tomshel.command.SimpleCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NoStickerCommand extends SimpleCommand implements OnlyAdminRestricted {
  public NoStickerCommand() {
    super(new SimpleCommandMeta().addAlias("безстиков").addAlias("банстикеров"));
  }

  @Override
  public void handle(TomasBot bot, String[] args, Update update) {
    bot.getChatStorage().setNoStickerMode(!bot.getChatStorage().isNoStickerMode());
    bot.sendBack(
        update,
        new SendMessage()
            .setText(
                "*Режим блокировки стикеров*: "
                    + (bot.getChatStorage().isNoStickerMode() ? "**ВКЛЮЧЕН**" : "выключен"))
            .enableMarkdown(true));
  }

  @Override
  public String getLabel() {
    return "bansticks";
  }
}
