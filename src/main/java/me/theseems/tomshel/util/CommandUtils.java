package me.theseems.tomshel.util;

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
}
