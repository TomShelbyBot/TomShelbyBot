package me.theseems.tomshel.storage;

import java.util.Collection;
import java.util.Optional;

public interface ChatMeta {
  void set(String key, Object value);

  Optional<Object> get(String key);

  Optional<Integer> getInteger(String key);

  Optional<Double> getDouble(String key);

  Optional<String> getString(String key);

  Optional<Boolean> getBoolean(String key);

  void remove(String key);

  Collection<String> getKeys();

  ChatMeta merge(ChatMeta other);

  void mergeInto(ChatMeta other);

  void replaceWith(ChatMeta other);
}
