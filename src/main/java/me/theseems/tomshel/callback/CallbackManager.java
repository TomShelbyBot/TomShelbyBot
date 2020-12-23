package me.theseems.tomshel.callback;

import me.theseems.tomshel.ThomasBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collection;
import java.util.Optional;

public interface CallbackManager {
  /**
   * Add callback handler
   *
   * @param handler to add
   */
  void addCallbackHandler(CallbackHandler handler);

  /**
   * Remove callback handler
   *
   * @param handler to remove
   */
  void removeCallbackHandler(CallbackHandler handler);

  /**
   * Call the callback propagation
   *
   * @param bot to handle for
   * @param update to handle
   */
  void call(ThomasBot bot, Update update);

  /**
   * Get handlers by prefix
   *
   * @param prefix to get by
   * @return handlers
   */
  Collection<CallbackHandler> getByPrefix(String prefix);

  /**
   * Get all handlers there are in the manager
   *
   * @return handlers
   */
  Collection<CallbackHandler> getHandlers();

  /**
   * Get handler by name
   *
   * @param name to get by
   * @return handler if there is
   */
  Optional<CallbackHandler> getHandler(String name);
}
