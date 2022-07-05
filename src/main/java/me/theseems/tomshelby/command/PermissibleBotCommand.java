package me.theseems.tomshelby.command;

public interface PermissibleBotCommand extends BotCommand {
  /**
   * Can a user use that command
   *
   * @param userId to check
   * @return verdict
   */
  boolean canUse(String chatId, Long userId);
}
