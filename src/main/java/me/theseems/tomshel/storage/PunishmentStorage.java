package me.theseems.tomshel.storage;

import me.theseems.tomshel.punishment.Punishment;
import me.theseems.tomshel.punishment.PunishmentType;

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
  Optional<Punishment> getActivePunishment(Integer userId, PunishmentType type);

  /**
   * Get active punishment by type
   *
   * @param userId to get for
   * @param types to get by
   * @return active punishment
   */
  Optional<Punishment> getAnyActivePunishment(Integer userId, PunishmentType... types);

  /**
   * Get active punishment for user
   *
   * @param userId to get for
   * @return punishment
   */
  boolean hasActivePunishment(Integer userId);

  /**
   * Add punishment to user
   * @param userId id of user
   * @param punishment to add
   */
  void addPunishment(Integer userId, Punishment punishment);

  /**
   * Get all punishments there are for user
   * @param userId to get for
   * @return punishments
   */
  Collection<Punishment> getPunishments(Integer userId);

  /**
   * Remove punishment from user
   * @param userId to remove from
   * @param punishment to remove
   */
  void removePunishment(Integer userId, Punishment punishment);
}
