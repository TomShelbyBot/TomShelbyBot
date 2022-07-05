package me.theseems.tomshelby.storage;

import me.theseems.tomshelby.punishment.Punishment;
import me.theseems.tomshelby.punishment.PunishmentType;

import java.util.*;

public class SimplePunishmentStorage implements PunishmentStorage {
    private final Map<Long, HashSet<Punishment>> punishmentMap;

    public SimplePunishmentStorage() {
        punishmentMap = new HashMap<>();
    }

    public void trim(Long userId) {
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
     * @param type   to get by
     * @return active punishment
     */
    @Override
    public Optional<Punishment> getActivePunishment(Long userId, PunishmentType type) {
        if (!punishmentMap.containsKey(userId)) return Optional.empty();
        trim(userId);

        return punishmentMap.get(userId).stream()
                .filter(punishment -> punishment.getType().equals(type))
                .findFirst();
    }

    /**
     * Get active punishment by type
     *
     * @param userId to get for
     * @param types  to get by
     * @return active punishment
     */
    @Override
    public Optional<Punishment> getAnyActivePunishment(Long userId, PunishmentType... types) {
        if (!punishmentMap.containsKey(userId)) {
            return Optional.empty();
        }

        trim(userId);

        return punishmentMap.get(userId).stream()
                .filter(
                        punishment -> {
                            for (PunishmentType type : types) {
                                if (punishment.getType().equals(type)) {
                                    return true;
                                }
                            }
                            return false;
                        })
                .findFirst();
    }

    /**
     * Get active punishment for user
     *
     * @param userId to get for
     * @return punishment
     */
    @Override
    public boolean hasActivePunishment(Long userId) {
        if (!punishmentMap.containsKey(userId)) {
            return false;
        }
        trim(userId);

        return !punishmentMap.get(userId).isEmpty();
    }

    /**
     * Add punishment to user
     *
     * @param userId     id of user
     * @param punishment to add
     */
    @Override
    public void addPunishment(Long userId, Punishment punishment) {
        if (!punishmentMap.containsKey(userId)) {
            punishmentMap.put(userId, new HashSet<>());
        }

        punishmentMap.get(userId).add(punishment);
        trim(userId);
    }

    /**
     * Get all punishments there are for user
     *
     * @param userId to get for
     * @return punishments
     */
    @Override
    public Collection<Punishment> getPunishments(Long userId) {
        if (punishmentMap.containsKey(userId)) {
            trim(userId);
        }
        return punishmentMap.containsKey(userId) ? punishmentMap.get(userId) : Collections.emptyList();
    }

    /**
     * Remove punishment from user
     *
     * @param userId     to remove from
     * @param punishment to remove
     */
    @Override
    public void removePunishment(Long userId, Punishment punishment) {
        if (!punishmentMap.containsKey(userId)) {
            return;
        }
        trim(userId);

        punishmentMap.get(userId).remove(punishment);
    }
}
