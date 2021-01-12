package me.theseems.tomshelby.storage;

import com.google.gson.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SimpleTomMeta implements TomMeta {
  private final JsonObject jsonObject;
  private final Map<String, TomMeta> metaMap;

  private JsonElement fromJavaObject(Object object) {
    JsonElement element;

    try {
      if (object instanceof SimpleTomMeta) {
        element = ((SimpleTomMeta) object).jsonObject;
      } else {
        element = new Gson().toJsonTree(object);
      }
    } catch (Exception e) {
      // In order we can't parse or other stuff has occurred
      element = JsonNull.INSTANCE;
    }

    if (element.isJsonNull()) return element.getAsJsonNull();
    if (element.isJsonObject()) return element.getAsJsonObject();
    if (element.isJsonArray()) return element.getAsJsonArray();
    return element.getAsJsonPrimitive();
  }

  private static JsonElement wrapJson(TomMeta meta, boolean pretty) {
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

    JsonObject object = new JsonObject();
    for (String s : simpleChatMeta.getKeys()) {
      if (simpleChatMeta.jsonObject.has(s)) {
        object.add(s, simpleChatMeta.jsonObject.get(s));
      } else if (simpleChatMeta.metaMap.containsKey(s)) {
        object.add(s, wrapJson(simpleChatMeta.metaMap.get(s), pretty));
      } else {
        object.add(s, JsonNull.INSTANCE);
      }
    }

    return object;
  }

  public static String jsonify(TomMeta meta) {
    return jsonify(meta, false);
  }

  public static String jsonify(TomMeta meta, boolean pretty) {
    return wrapJson(meta, pretty).toString();
  }

  public SimpleTomMeta() {
    jsonObject = new JsonObject();
    metaMap = new HashMap<>();
  }

  public SimpleTomMeta(JsonObject object) {
    jsonObject = object.deepCopy();
    metaMap = new HashMap<>();
  }

  public SimpleTomMeta(TomMeta other) {
    this();
    for (String key : other.getKeys()) {
      other.get(key).ifPresent(value -> jsonObject.add(key, fromJavaObject(value)));
    }
  }

  @Override
  public void set(String key, Object value) {
    jsonObject.add(key, fromJavaObject(value));
  }

  @Override
  public void set(String key, TomMeta value) {
    metaMap.put(key, value);
  }

  @Override
  public void set(String key, Object[] values) {
    JsonArray array = new JsonArray();
    for (Object object : values) {
      array.add(fromJavaObject(object));
    }

    jsonObject.add(key, array);
  }

  @Override
  public Optional<Object> get(String key) {
    if (!jsonObject.has(key)) return Optional.ofNullable(metaMap.get(key));
    return Optional.ofNullable(jsonObject.get(key));
  }

  private Optional<Integer> getRawInteger(String key) {
    if (!jsonObject.has(key)) return Optional.empty();
    try {
      return Optional.of(jsonObject.get(key).getAsInt());
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

  @Override
  public Optional<String> getString(String key) {
    if (!jsonObject.has(key)) return Optional.empty();
    try {
      return Optional.of(jsonObject.get(key).getAsString());
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<Boolean> getBoolean(String key) {
    if (!jsonObject.has(key)) return Optional.empty();
    try {
      return Optional.of(jsonObject.get(key).getAsBoolean());
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<TomMeta> getContainer(String key) {
    return Optional.ofNullable(metaMap.get(key));
  }

  public Optional<JsonArray> getJsonArray(String key) {
    if (!jsonObject.has(key)) return Optional.empty();
    if (!jsonObject.get(key).isJsonArray()) return Optional.empty();

    return Optional.ofNullable(jsonObject.getAsJsonArray(key));
  }

  public <T> Optional<List<T>> unpackJsonArray(String key, Function<JsonElement, T> filter) {
    if (!jsonObject.has(key)) return Optional.empty();
    if (!jsonObject.get(key).isJsonArray()) return Optional.empty();

    JsonArray array = jsonObject.get(key).getAsJsonArray();
    List<JsonElement> jsonElements = new ArrayList<>();

    for (JsonElement element : array) {
      jsonElements.add(element);
    }

    return Optional.of(
        jsonElements.stream().map(filter).filter(Objects::nonNull).collect(Collectors.toList()));
  }

  @Override
  public Optional<Integer[]> getIntegerArray(String key) {
    return unpackJsonArray(
            key,
            jsonElement -> {
              if (!jsonElement.isJsonPrimitive()) return null;
              return jsonElement.getAsInt();
            })
        .flatMap(integers -> Optional.of(integers.toArray(new Integer[0])));
  }

  @Override
  public Optional<Double[]> getDoubleArray(String key) {
    return unpackJsonArray(
            key,
            jsonElement -> {
              if (!jsonElement.isJsonPrimitive()) return null;
              return jsonElement.getAsDouble();
            })
        .flatMap(integers -> Optional.of(integers.toArray(new Double[0])));
  }

  @Override
  public Optional<String[]> getStringArray(String key) {
    return unpackJsonArray(
            key,
            jsonElement -> {
              if (!jsonElement.isJsonPrimitive()) return null;
              return jsonElement.getAsString();
            })
        .flatMap(integers -> Optional.of(integers.toArray(new String[0])));
  }

  @Override
  public Optional<Boolean[]> getBooleanArray(String key) {
    return unpackJsonArray(
            key,
            jsonElement -> {
              if (!jsonElement.isJsonPrimitive()) return null;
              return jsonElement.getAsBoolean();
            })
        .flatMap(integers -> Optional.of(integers.toArray(new Boolean[0])));
  }

  @Override
  public Optional<TomMeta[]> getContainerArray(String key) {
    return unpackJsonArray(
            key,
            jsonElement -> {
              if (!jsonElement.isJsonObject()) return null;
              return new SimpleTomMeta(jsonElement.getAsJsonObject());
            })
        .flatMap(integers -> Optional.of(integers.toArray(new TomMeta[0])));
  }

  @Override
  public void remove(String key) {
    jsonObject.remove(key);
  }

  @Override
  public Collection<String> getKeys() {
    Set<String> strings = new HashSet<>(jsonObject.keySet());
    strings.addAll(metaMap.keySet());
    return strings;
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
      if (!jsonObject.has(key)) {
        other.get(key).ifPresent(value -> set(key, value));
      }
    }
  }

  @Override
  public Optional<Double> getDouble(String key) {
    if (!jsonObject.has(key)) return Optional.empty();
    try {
      return Optional.of(Double.parseDouble(jsonObject.get(key).toString()));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  @Override
  public void replaceWith(TomMeta other) {
    clear();
    for (String key : other.getKeys()) {
      other.get(key).ifPresent(value -> jsonObject.add(key, fromJavaObject(value)));
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SimpleTomMeta meta = (SimpleTomMeta) o;
    return jsonObject.equals(meta.jsonObject);
  }

  private void clear() {
    for (String s : jsonObject.keySet()) {
      jsonObject.remove(s);
    }
  }

  public JsonObject toJsonObject() {
    return jsonObject;
  }

  @Override
  public int hashCode() {
    return Objects.hash(jsonObject);
  }

  @Override
  public String toString() {
    return "SimpleTomMeta{" + "jsonObject=" + jsonObject + ", metaMap=" + metaMap + '}';
  }
}
