package me.theseems.tomshel.update;

import me.theseems.tomshel.ThomasBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collection;

public interface UpdateHandlerManager {
  /**
   * Add update handler
   *
   * @param handler to add
   */
  void addUpdateHandler(UpdateHandler handler);

  /**
   * Remove update handler
   *
   * @param handler to remove
   */
  void removeUpdateHandler(UpdateHandler handler);

  /**
   * Handle update
   *
   * @param bot to handle for
   * @param update to handle
   * @return handle result whether we should go next (in processing and update) or not
   */
  boolean handleUpdate(ThomasBot bot, Update update);

  /**
   * Get all update handlers there are
   *
   * @return handlers
   */
  Collection<UpdateHandler> getUpdateHandlers();
}
