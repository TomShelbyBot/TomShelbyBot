package me.theseems.tomshel.punishment;

import java.util.Optional;

public interface Punishment {
  /**
   * Get type of punishment
   *
   * @return type
   */
  PunishmentType getType();

  /**
   * Is punishment active
   *
   * @return is active
   */
  boolean isActive();

  /**
   * Get reason of punishment
   *
   * @return reason
   */
  Optional<String> getReason();
}
