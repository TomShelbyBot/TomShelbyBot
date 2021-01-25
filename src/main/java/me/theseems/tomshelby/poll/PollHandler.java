package me.theseems.tomshelby.poll;

import me.theseems.tomshelby.ThomasBot;

public interface PollHandler {
  /**
   * Handle poll change (or anything related to polls)
   *
   * @param pollUpdate to handle
   */
  void handlePollUpdate(ThomasBot bot, PollUpdate pollUpdate);
}
