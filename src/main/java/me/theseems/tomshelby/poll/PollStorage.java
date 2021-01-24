package me.theseems.tomshelby.poll;

import java.util.Optional;

public interface PollStorage {
  /**
   * Put poll to the storage
   *
   * @param botPoll to put
   */
  void put(BotPoll botPoll);

  /**
   * Remove poll from storage
   *
   * @param botPoll to remove
   */
  void remove(BotPoll botPoll);

  /**
   * Get poll by it's identity
   *
   * @param pollId to get by
   * @return poll if found
   */
  Optional<BotPoll> getPoll(String pollId);
}
