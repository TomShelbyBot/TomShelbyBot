package me.theseems.tomshelby;

import me.theseems.tomshelby.util.CommandUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringsTest {
  @Test
  public void extractCommandTest() {
    String sample = "/mute @all for  that being said!";
    CommandUtils.CommandSkeleton skeleton = CommandUtils.extractCommand(sample, "sample");

    System.out.println(skeleton);
    Assertions.assertEquals(skeleton.label, "mute");
    Assertions.assertArrayEquals(skeleton.args, new String[]{"@all", "for", "that", "being", "said!"});

    sample = "/mute@test @all for that being said!";
    skeleton = CommandUtils.extractCommand(sample, "test");

    Assertions.assertEquals(skeleton.label, "mute");
    Assertions.assertTrue(skeleton.accessExplicit);
    Assertions.assertArrayEquals(skeleton.args, new String[]{"@all", "for", "that", "being", "said!"});
  }
}
