package me.theseems.tomshel.util;

import com.google.common.base.Splitter;
import me.theseems.tomshel.Main;

import java.util.Arrays;

public class CommandUtils {

  public static class BotCommandException extends IllegalStateException {
    private String message;

    public BotCommandException(String message) {
      this.message = message;
    }

    @Override
    public String getMessage() {
      return message;
    }
  }

  public static class NumberCommandException extends BotCommandException {
    public NumberCommandException() {
      super("Пожалуйста, укажите число!");
    }
  }

  public static int requireInt(String str) {
    try {
      return Integer.parseInt(str);
    } catch (NumberFormatException e) {
      throw new NumberCommandException();
    }
  }

  public static long requireLong(String str) {
    try {
      return Long.parseLong(str);
    } catch (NumberFormatException e) {
      throw new NumberCommandException();
    }
  }

  public static int requireInt(String str, String errorMessage) {
    try {
      return Integer.parseInt(str);
    } catch (NumberFormatException e) {
      throw new BotCommandException(errorMessage);
    }
  }

  public static int requirePositiveInt(String str, String errorMessage) {
    try {
      int result = Integer.parseInt(str);
      if (result <= 0) throw new BotCommandException(errorMessage);
      return result;
    } catch (NumberFormatException e) {
      throw new BotCommandException(errorMessage);
    }
  }

  public static int requireNonNegativeInt(String str, String errorMessage) {
    try {
      int result = Integer.parseInt(str);
      if (result < 0) throw new BotCommandException(errorMessage);
      return result;
    } catch (NumberFormatException e) {
      throw new BotCommandException(errorMessage);
    }
  }

  public static class CommandSkeleton {
    public String label;
    public String[] args;
    public boolean success;
    public boolean isSpecificToBot;

    @Override
    public String toString() {
      return "CommandSkeleton{" +
          "label='" + label + '\'' +
          ", args=" + Arrays.toString(args) +
          ", success=" + success +
          ", isSpecificToBot=" + isSpecificToBot +
          '}';
    }
  }

  public static CommandSkeleton extractCommand(String text) {
    CommandSkeleton skeleton = new CommandSkeleton();
    skeleton.success = false;

    if (text == null || text.isEmpty()) return skeleton;

    String[] args = Splitter.on(' ').omitEmptyStrings().trimResults().splitToList(text).toArray(new String[]{});
    String label = args[0];

    if (!label.startsWith("/")) return skeleton;

    label = label.substring(1);
    if (label.isEmpty()) return skeleton;

    skeleton.success = true;
    if (label.endsWith("@" + Main.getBot().getBotUsername())) {
      label = label.substring(0, Math.max(1, label.length() - Main.getBot().getBotUsername().length() - 1));
      skeleton.isSpecificToBot = true;
    }

    skeleton.label = label;
    if (args.length == 1) {
      skeleton.args = new String[]{};
    } else {
      skeleton.args = StringUtils.skipOne(args);
    }

    return skeleton;
  }
}
