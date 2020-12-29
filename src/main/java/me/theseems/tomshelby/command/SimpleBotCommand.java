package me.theseems.tomshelby.command;

import me.theseems.tomshelby.ThomasBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class SimpleBotCommand implements BotCommand {
  private final CommandMeta meta;

  public SimpleBotCommand(CommandMeta meta) {
    this.meta = meta;
  }

  /**
   * Handle update for that command
   *
   * @param bot to handle with
   * @param update to handle
   */
  @Override
  abstract public void handle(ThomasBot bot, String[] args, Update update);

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
