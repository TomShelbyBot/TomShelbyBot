package me.theseems.tomshelby.poll;

import me.theseems.tomshelby.ThomasBot;

import java.util.Collection;

public interface PollManager {
  /**
   * Get poll's storage
   *
   * @return storage
   */
  PollStorage getStorage();

  /**
   * Get all handlers there are
   *
   * @return handlers
   */
  Collection<PollHandler> getHandlers();

  /**
   * Add poll handler
   *
   * @param pollHandler to add
   */
  void addHandler(PollHandler pollHandler);

  /**
   * Remove handler
   *
   * @param pollHandler to remove
   */
  void removeHandler(PollHandler pollHandler);

  /**
   * Handle poll update
   *
   * @param pollUpdate to handle
   */
  default void handleUpdate(ThomasBot bot, PollUpdate pollUpdate) {
    for (PollHandler handler : getHandlers()) {
      handler.handlePollUpdate(bot, pollUpdate);
    }
  }
}
