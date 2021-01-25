package me.theseems.tomshelby.util;

import me.theseems.tomshelby.storage.SimpleTomMeta;
import me.theseems.tomshelby.storage.TomMeta;

public class MetaBuilder {
  private final TomMeta meta;

  public MetaBuilder(TomMeta tomMeta) {
    this.meta = tomMeta;
  }

  public static MetaBuilder on(TomMeta meta) {
    return new MetaBuilder(meta);
  }

  public static MetaBuilder simple() {
    return new MetaBuilder(new SimpleTomMeta());
  }

  public MetaBuilder and(String key, Object value) {
    meta.set(key, value);
    return this;
  }

  public MetaBuilder and(String key, Object[] value) {
    meta.set(key, value);
    return this;
  }

  public MetaBuilder and(String key, TomMeta container) {
    meta.set(key, container);
    return this;
  }

  public TomMeta build() {
    return meta;
  }
}
