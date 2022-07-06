package me.theseems.tomshelby.storage.adapters;

import com.google.gson.*;
import me.theseems.tomshelby.storage.SimpleTomMeta;

import java.lang.reflect.Type;

public class SimpleTomMetaAdapter
    implements JsonSerializer<SimpleTomMeta>, JsonDeserializer<SimpleTomMeta> {
  @Override
  public JsonElement serialize(
      SimpleTomMeta src, Type typeOfSrc, JsonSerializationContext context) {
    return SimpleTomMeta.wrapJson(src);
  }

  @Override
  public SimpleTomMeta deserialize(
      JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    return new SimpleTomMeta(json.getAsJsonObject());
  }
}
