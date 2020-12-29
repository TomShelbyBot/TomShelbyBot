package me.theseems.tomshelby.util;

import me.theseems.tomshelby.Main;

import java.util.Arrays;
import java.util.stream.Stream;

public class CommandUtils {

  public static class BotCommandException extends IllegalStateException {
    private final String message;

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
    public boolean accessExplicit;

    @Override
    public String toString() {
      return "CommandSkeleton{"
          + "label='"
          + label
          + '\''
          + ", args="
          + Arrays.toString(args)
          + ", success="
          + success
          + ", accessExplicit="
          + accessExplicit
          + '}';
    }
  }

  public static CommandSkeleton extractCommand(String text, String botUsername) {
    CommandSkeleton skeleton = new CommandSkeleton();

    String[] args = Stream.of(text.split(" ")).filter(w -> !w.isEmpty()).toArray(String[]::new);
    String potentialLabel = args[0];

    // Command must start with '/' symbol
    if (!potentialLabel.startsWith("/")) return skeleton;

    String label = args[0].substring(1);
    boolean explicit = label.endsWith(botUsername);

    // Skip the label
    args = StringUtils.skipOne(args);

    // If we have such: /command@bot_username
    if (explicit) {
      label = label.substring(0, Math.max(1, label.length() - botUsername.length() - 1));
    }

    skeleton.success = true;
    skeleton.label = label;
    skeleton.accessExplicit = explicit;
    skeleton.args = args;
    return skeleton;
  }

  public static CommandSkeleton extractCommand(String text) {
    return extractCommand(text, Main.getBot().getBotUsername());
  }
}
