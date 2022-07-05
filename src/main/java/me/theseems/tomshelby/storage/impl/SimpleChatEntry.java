package me.theseems.tomshelby.storage.impl;

import me.theseems.tomshelby.storage.SimpleTomMeta;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SimpleChatEntry {
  private final Map<String, Long> users;
  private final SimpleTomMeta meta;

  public SimpleChatEntry() {
    users = new HashMap<String, Long>();
    meta = new SimpleTomMeta();
  }

  public boolean containsUser(String username) {
    return users.containsKey(username);
  }

  public Optional<Long> getUserId(String username) {
    return Optional.ofNullable(users.get(username));
  }

  public Collection<String> getUserNicknames() {
    return users.keySet();
  }

  public Collection<Long> getUserIds() {
    return users.values();
  }

  public void putUser(String username, Long userId) {
    users.put(username, userId);
  }

  public SimpleTomMeta getMeta() {
    return meta;
  }
}
