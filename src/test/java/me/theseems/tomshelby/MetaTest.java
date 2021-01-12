package me.theseems.tomshelby;

import me.theseems.tomshelby.storage.SimpleTomMeta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class MetaTest {
  @Test
  public void simpleTomMeta_simplePut() {
    SimpleTomMeta meta = new SimpleTomMeta();
    final String[] strings = new String[] {"a", "b", "c", "d"};

    meta.set("key1", "value1");
    meta.set("key2", strings);
    meta.set("key3", true);
    meta.set("key4", 0b1);
    meta.set("key5", 1.f);

    Assertions.assertEquals("value1", meta.getString("key1").orElse(null));
    Assertions.assertArrayEquals(strings, meta.getStringArray("key2").orElse(null));

    System.out.println(meta.toString());
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

    SimpleTomMeta[] containers = new SimpleTomMeta[] {sample, sample, sample, sample};
    meta.set("key4", containers);

    Assertions.assertArrayEquals(meta.getContainerArray("key4").orElse(null), containers);
  }

  @Test
  public void simpleTomMeta_linkTest() {
    SimpleTomMeta global = new SimpleTomMeta();
    SimpleTomMeta local = new SimpleTomMeta();

    global.set("key", local);
    local.set("key1", "value1");

    Assertions.assertTrue(
        global.get("key").isPresent()
            && global.getContainer("key").isPresent()
            && Objects.requireNonNull(
                    global.getContainer("key").get().getString("key1").orElse(null))
                .equals("value1"));

    local.set("key2", "value2");

    Assertions.assertEquals("{\"key\":{\"key1\":\"value1\",\"key2\":\"value2\"}}", SimpleTomMeta.jsonify(global, true));
  }
}
