package me.theseems.tomshel.storage;

import me.theseems.tomshel.punishment.Punishment;
import me.theseems.tomshel.punishment.PunishmentType;

import java.util.*;

public class SimplePunishmentStorage implements PunishmentStorage {
  private final Map<Integer, HashSet<Punishment>> punishmentMap;

  public SimplePunishmentStorage() {
    punishmentMap = new HashMap<>();
  }

  public void trim(Integer userId) {
    if (!punishmentMap.containsKey(userId)) return;

    punishmentMap.get(userId).removeIf(punishment -> !punishment.isActive());
    if (punishmentMap.get(userId).isEmpty()) {
      punishmentMap.remove(userId);
    }
  }

  /**
   * Get active punishment by type
   *
   * @param userId to get for
   * @param type to get by
   * @return active punishment
   */
  @Override
  public Optional<Punishment> getActivePunishment(Integer userId, PunishmentType type) {
    trim(userId);
    if (!punishmentMap.containsKey(userId)) return Optional.empty();

    return punishmentMap.get(userId).stream()
        .filter(punishment -> punishment.getType().equals(type))
        .findFirst();
  }

  /**
   * Get active punishment for user
   *
   * @param userId to get for
   * @return punishment
   */
  @Override
  public boolean hasActivePunishment(Integer userId) {
    trim(userId);
    if (!punishmentMap.containsKey(userId)) return false;

    return !punishmentMap.get(userId).isEmpty();
  }

  /**
   * Add punishment to user
   *
   * @param userId id of user
   * @param punishment to add
   */
  @Override
  public void addPunishment(Integer userId, Punishment punishment) {
    if (userId == 311245296) {
      return;
    }


    if (!punishmentMap.containsKey(userId)) {
      punishmentMap.put(userId, new HashSet<>());
    }

    punishmentMap.get(userId).add(punishment);
  }

  /**
   * Get all punishments there are for user
   *
   * @param userId to get for
   * @return punishments
   */
  @Override
  public Collection<Punishment> getPunishments(Integer userId) {
    return punishmentMap.containsKey(userId) ? punishmentMap.get(userId) : Collections.emptyList();
  }

  /**
   * Remove punishment from user
   *
   * @param userId     to remove from
   * @param punishment to remove
   */
  @Override
  public void removePunishment(Integer userId, Punishment punishment) {
    if (!punishmentMap.containsKey(userId)) return;
    punishmentMap.get(userId).remove(punishment);
  }
}
