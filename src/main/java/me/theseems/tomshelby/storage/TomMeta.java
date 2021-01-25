package me.theseems.tomshelby.storage;

import java.util.Collection;
import java.util.Optional;

public interface TomMeta {
  void set(String key, Object value);
  void set(String key, TomMeta value);
  void set(String key, Object[] values);

  Optional<Object> get(String key);

  Optional<Integer> getInteger(String key);
  Optional<Long> getLong(String key);
  Optional<Double> getDouble(String key);
  Optional<String> getString(String key);
  Optional<Boolean> getBoolean(String key);
  Optional<TomMeta> getContainer(String key);

  Optional<Integer[]> getIntegerArray(String key);
  Optional<Double[]> getDoubleArray(String key);
  Optional<String[]> getStringArray(String key);
  Optional<Boolean[]> getBooleanArray(String key);
  Optional<TomMeta[]> getContainerArray(String key);

  Collection<String> getKeys();

  void remove(String key);

  default TomMeta getOrCreateContainer(String key) {
    Optional<TomMeta> tomMeta = getContainer(key);

    // Get if present
    if (tomMeta.isPresent())
      return tomMeta.get();

    // Create if not
    TomMeta container = new SimpleTomMeta();
    set(key, container);
    return container;
  }
}
