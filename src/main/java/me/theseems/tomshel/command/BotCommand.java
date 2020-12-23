package me.theseems.tomshel.command;

import me.theseems.tomshel.ThomasBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotCommand {
  /**
   * Handle update for that command
   * @param update to handle
   */
  void handle(ThomasBot bot, String[] args, Update update);

  /**
   * Get meta of the command
   * @return meta
   */
  CommandMeta getMeta();
}