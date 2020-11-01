package me.theseems.tomshel;

import me.theseems.tomshel.util.CommandUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringsTest {
  @Test
  public void extractCommandTest() {
    Main.initialize();

    String sample = "/mute @all for  that being said!";
    CommandUtils.CommandSkeleton skeleton = CommandUtils.extractCommand(sample);

    Assertions.assertEquals(skeleton.label, "mute");
    Assertions.assertArrayEquals(skeleton.args, new String[]{"@all", "for", "that", "being", "said!"});

    sample = "/mute@" + Main.getBot().getBotUsername() + " @all for that being said!";
    skeleton = CommandUtils.extractCommand(sample);
  }
}
