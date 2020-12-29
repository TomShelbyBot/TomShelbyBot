package me.theseems.tomshelby.callback;

import me.theseems.tomshelby.ThomasBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CallbackHandler {
  /**
   * Handle callback
   *
   * @param bot to handle for
   * @param update to handle
   * @param args a string to handle
   */
  void onCallback(ThomasBot bot, Update update, String[] args);

  /**
   * Get name of callback handler
   *
   * @return name
   */
  String getName();

  /**
   * Get prefix of callback to handle
   *
   * @return prefix
   */
  String getPrefix();
}
