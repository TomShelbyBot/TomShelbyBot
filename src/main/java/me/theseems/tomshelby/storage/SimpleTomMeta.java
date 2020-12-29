package me.theseems.tomshelby.storage;

import com.google.gson.GsonBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleTomMeta implements TomMeta {
  private final Map<String, Object> map;

  public SimpleTomMeta() {
    map = new ConcurrentHashMap<>();
  }

  public SimpleTomMeta(TomMeta other) {
    this();
    for (String key : other.getKeys()) {
      other.get(key).ifPresent(value -> map.put(key, value));
    }
  }

  @Override
  public void set(String key, Object value) {
    map.put(key, value);
  }

  @Override
  public Optional<Object> get(String key) {
    return Optional.ofNullable(map.get(key));
  }

  private Optional<Integer> getRawInteger(String key) {
    if (!map.containsKey(key)) return Optional.empty();
    try {
      return Optional.of((Integer) map.get(key));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<Integer> getInteger(String key) {
    Optional<Integer> optionalInteger = getRawInteger(key);
    if (optionalInteger.isPresent()) return optionalInteger;
    else {
      return getDouble(key).map(Double::intValue);
    }
  }

  public static String jsonify(TomMeta meta) {
    return jsonify(meta, true);
  }

  public static String jsonify(TomMeta meta, boolean pretty) {
    SimpleTomMeta simpleChatMeta;
    if (meta instanceof SimpleTomMeta) {
      simpleChatMeta = (SimpleTomMeta) meta;
    } else {
      simpleChatMeta = new SimpleTomMeta(meta);
    }

    GsonBuilder builder = new GsonBuilder();
    if (pretty) {
      builder.setPrettyPrinting();
    }

    return builder.create().toJson(simpleChatMeta.map, Map.class);
  }

  @Override
  public Optional<String> getString(String key) {
    if (!map.containsKey(key)) return Optional.empty();
    try {
      return Optional.of((String) map.get(key));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<Boolean> getBoolean(String key) {
    if (!map.containsKey(key)) return Optional.empty();
    try {
      return Optional.of((Boolean) map.get(key));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  @Override
  public void remove(String key) {
    map.remove(key);
  }

  @Override
  public Collection<String> getKeys() {
    return map.keySet();
  }

  @Override
  public TomMeta merge(TomMeta other) {
    TomMeta meta = new SimpleTomMeta(this);
    meta.mergeInto(other);
    return meta;
  }

  @Override
  public void mergeInto(TomMeta other) {
    for (String key : other.getKeys()) {
      if (!map.containsKey(key)) {
        other.get(key).ifPresent(value -> set(key, value));
      }
    }
  }

  @Override
  public Optional<Double> getDouble(String key) {
    if (!map.containsKey(key)) return Optional.empty();
    try {
      return Optional.of(Double.parseDouble(map.get(key).toString()));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  @Override
  public void replaceWith(TomMeta other) {
    map.clear();
    for (String key : other.getKeys()) {
      other.get(key).ifPresent(value -> map.put(key, value));
    }
  }
}
