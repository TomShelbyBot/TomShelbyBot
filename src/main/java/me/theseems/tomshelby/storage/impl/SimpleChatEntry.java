package me.theseems.tomshelby.storage.impl;

import me.theseems.tomshelby.storage.SimpleTomMeta;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SimpleChatEntry {
  private final Map<String, Integer> users;
  private final SimpleTomMeta meta;

  public SimpleChatEntry() {
    users = new HashMap<>();
    meta = new SimpleTomMeta();
  }

  public boolean containsUser(String username) {
    return users.containsKey(username);
  }

  public Optional<Integer> getUserId(String username) {
    return Optional.ofNullable(users.get(username));
  }

  public Collection<String> getUserNicknames() {
    return users.keySet();
  }

  public Collection<Integer> getUserIds() {
    return users.values();
  }

  public void putUser(String username, Integer userId) {
    users.put(username, userId);
  }

  public SimpleTomMeta getMeta() {
    return meta;
  }
}
