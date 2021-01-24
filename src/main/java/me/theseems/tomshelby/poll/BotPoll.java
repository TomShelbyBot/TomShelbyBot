package me.theseems.tomshelby.poll;

import me.theseems.tomshelby.storage.TomMeta;

public interface BotPoll {
  /**
   * Identity (the exact message in exact chat)
   * @return identity
   */
  MessageIdentity getIdentity();

  /**
   * Get poll id
   * @return id
   */
  String getId();

  /**
   * Get metadata attached to that poll
   *
   * @return meta
   */
  TomMeta getMeta();
}
