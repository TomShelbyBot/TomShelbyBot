package me.theseems.tomshel.command;

import me.theseems.tomshel.TomasBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class SimpleCommand implements Command {
  private final CommandMeta meta;

  public SimpleCommand(CommandMeta meta) {
    this.meta = meta;
  }

  /**
   * Handle update for that command
   *
   * @param bot to handle with
   * @param update to handle
   */
  @Override
  abstract public void handle(TomasBot bot, String[] args, Update update);

  /**
   * Get meta of the command
   *
   * @return meta
   */
  @Override
  public CommandMeta getMeta() {
    return meta;
  }
}
