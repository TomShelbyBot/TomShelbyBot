package me.theseems.tomshel.command;

public interface Permissible {
  /**
   * Can a user use that command
   * @param userId to check
   * @return verdict
   */
  boolean canUse(Long chatId, Integer userId);
}
