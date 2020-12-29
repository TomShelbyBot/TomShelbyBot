package me.theseems.tomshelby.callback;

import me.theseems.tomshelby.ThomasBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class SimpleCallbackHandler implements CallbackHandler {
  private final String name;
  private final String prefix;

  public SimpleCallbackHandler(String name, String prefix) {
    this.name = name;
    this.prefix = prefix;
  }

  /**
   * Handle callback
   *
   * @param bot to handle for
   * @param update to handle
   * @param args to handle with
   */
  @Override
  public abstract void onCallback(ThomasBot bot, Update update, String[] args);

  /**
   * Get name of callback handler
   *
   * @return name
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Get prefix of callback to handle
   *
   * @return prefix
   */
  @Override
  public String getPrefix() {
    return prefix;
  }
}
