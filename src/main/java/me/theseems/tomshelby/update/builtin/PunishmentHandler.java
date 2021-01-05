package me.theseems.tomshelby.update.builtin;


import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.update.SimpleUpdateHandler;
import org.telegram.telegrambots.meta.api.objects.Update;

public class PunishmentHandler extends SimpleUpdateHandler {
  @Override
  public boolean handleUpdate(ThomasBot bot, Update update) {
    return bot.getPunishmentHandler().handle(update);
  }
}
