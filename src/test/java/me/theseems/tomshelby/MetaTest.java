package me.theseems.tomshelby;

import me.theseems.tomshelby.storage.SimpleTomMeta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MetaTest {
  @Test
  public void simpleTomMeta_simplePut() {
    SimpleTomMeta meta = new SimpleTomMeta();
    final String[] strings = new String[]{"a", "b", "c", "d"};

    meta.set("key1", "value1");
    meta.set("key2", strings);
    meta.set("key3", true);
    meta.set("key4", 0b1);
    meta.set("key5", 1.f);

    Assertions.assertEquals("value1", meta.getString("key1").orElse(null));
    Assertions.assertArrayEquals(strings, meta.getStringArray("key2").orElse(null));
  }

  @Test
  public void simpleTomMeta_recursivePut() {
    SimpleTomMeta meta = new SimpleTomMeta();
    meta.set("key1", "value1");

    SimpleTomMeta copy = new SimpleTomMeta(meta);
    meta.set("key2", meta);
    SimpleTomMeta anotherCopy = new SimpleTomMeta(meta);

    Assertions.assertEquals("value1", meta.getString("key1").orElse(null));
    Assertions.assertEquals(copy, meta.getContainer("key2").orElse(null));

    Assertions.assertEquals(meta, anotherCopy);
    anotherCopy.set("key3", "value3");
    Assertions.assertNotEquals(meta, anotherCopy);


    SimpleTomMeta sample = new SimpleTomMeta();
    sample.set("key", "value");

    SimpleTomMeta[] containers = new SimpleTomMeta[]{sample, sample, sample, sample};
    meta.set("key4", containers);

    Assertions.assertArrayEquals(meta.getContainerArray("key4").orElse(null), containers);
  }
}
