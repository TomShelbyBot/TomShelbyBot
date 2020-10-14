package me.theseems.tomshel.command;

import me.theseems.tomshel.TomasBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {
  /**
   * Handle update for that command
   * @param update to handle
   */
  void handle(TomasBot bot, String[] args, Update update);

  /**
   * Get meta of the command
   * @return meta
   */
  CommandMeta getMeta();
}
