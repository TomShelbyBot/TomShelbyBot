package me.theseems.tomshel.punishment;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface PunishmentProcessor {
  /**
   * Handle punishment
   *
   * @param update to handle
   * @param punishment of user to handle
   * @return should proceed
   */
  boolean handle(Update update, Punishment punishment);

  /**
   * Get type of punishment to handle
   *
   * @return type
   */
  PunishmentType getType();

  /**
   * Get name of processor
   *
   * @return name
   */
  String getName();

  /**
   * Get priority of the processor
   *
   * @return priority
   */
  int getPriority();
}
