package me.theseems.tomshelby.poll;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MetaPollManager implements PollManager {
  private final PollStorage pollStorage;
  private final List<PollHandler> handlerList;

  public MetaPollManager(PollStorage pollStorage) {
    this.handlerList = new ArrayList<>();
    this.pollStorage = pollStorage;
  }

  /**
   * Get poll's storage
   *
   * @return storage
   */
  @Override
  public PollStorage getStorage() {
    return pollStorage;
  }

  /**
   * Get all handlers there are
   *
   * @return handlers
   */
  @Override
  public Collection<PollHandler> getHandlers() {
    return Collections.unmodifiableList(handlerList);
  }

  /**
   * Add poll handler
   *
   * @param pollHandler to add
   */
  @Override
  public void addHandler(PollHandler pollHandler) {
    handlerList.add(pollHandler);
  }

  /**
   * Remove handler
   *
   * @param pollHandler to remove
   */
  @Override
  public void removeHandler(PollHandler pollHandler) {
    handlerList.remove(pollHandler);
  }
}
