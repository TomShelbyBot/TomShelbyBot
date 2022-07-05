package me.theseems.tomshelby.storage;

import me.theseems.tomshelby.punishment.Punishment;
import me.theseems.tomshelby.punishment.PunishmentType;

import java.util.Collection;
import java.util.Optional;

public interface PunishmentStorage {
  /**
   * Get active punishment by type
   *
   * @param userId to get for
   * @param type to get by
   * @return active punishment
   */
  Optional<Punishment> getActivePunishment(Long userId, PunishmentType type);

  /**
   * Get active punishment by type
   *
   * @param userId to get for
   * @param types to get by
   * @return active punishment
   */
  Optional<Punishment> getAnyActivePunishment(Long userId, PunishmentType... types);

  /**
   * Get active punishment for user
   *
   * @param userId to get for
   * @return punishment
   */
  boolean hasActivePunishment(Long userId);

  /**
   * Add punishment to user
   *
   * @param userId id of user
   * @param punishment to add
   */
  void addPunishment(Long userId, Punishment punishment);

  /**
   * Get all punishments there are for user
   *
   * @param userId to get for
   * @return punishments
   */
  Collection<Punishment> getPunishments(Long userId);

  /**
   * Remove punishment from user
   *
   * @param userId to remove from
   * @param punishment to remove
   */
  void removePunishment(Long userId, Punishment punishment);
}
