package me.theseems.tomshel.update;

import me.theseems.tomshel.ThomasBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateHandler {
  /**
   * Handle update
   * @param bot to handle for
   * @param update to handle
   * @return whether we should process update next or not
   */
  boolean handleUpdate(ThomasBot bot, Update update);

  /**
   * Get priority
   * @return priority
   */
  int getPriority();
}
