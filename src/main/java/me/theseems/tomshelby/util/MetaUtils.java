package me.theseems.tomshelby.util;

import me.theseems.tomshelby.storage.TomMeta;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class MetaUtils {
  public static Optional<Date> getDate(String key, TomMeta meta, DateFormat format) {
    return meta.getString(key).flatMap(s -> {
      try {
        return Optional.of(format.parse(s));
      } catch (ParseException e) {
        return Optional.empty();
      }
    });
  }

  public static Optional<Date> getDate(String key, TomMeta meta) {
    return getDate(key, meta, new SimpleDateFormat());
  }

  public static void setDate(String key, Date value, TomMeta meta, DateFormat format) {
    meta.set(key, format.format(value));
  }

  public static void setDate(String key, Date value, TomMeta meta) {
    setDate(key, value, meta, new SimpleDateFormat());
  }
}
