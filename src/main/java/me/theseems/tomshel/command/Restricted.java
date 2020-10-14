package me.theseems.tomshel.command;

public interface Restricted {
  /**
   * Can a user use that command
   * @param userId to check
   * @return verdict
   */
  boolean canUse(Long chatId, Integer userId);
}
