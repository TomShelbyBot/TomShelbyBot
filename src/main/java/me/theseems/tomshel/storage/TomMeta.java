package me.theseems.tomshel.storage;

import java.util.Collection;
import java.util.Optional;

public interface TomMeta {
  void set(String key, Object value);

  Optional<Object> get(String key);

  Optional<Integer> getInteger(String key);

  Optional<Double> getDouble(String key);

  Optional<String> getString(String key);

  Optional<Boolean> getBoolean(String key);

  void remove(String key);

  Collection<String> getKeys();

  TomMeta merge(TomMeta other);

  void mergeInto(TomMeta other);

  void replaceWith(TomMeta other);
}
