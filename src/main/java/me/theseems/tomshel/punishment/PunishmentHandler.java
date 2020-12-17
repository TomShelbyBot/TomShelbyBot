package me.theseems.tomshel.punishment;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface PunishmentHandler {
    /**
     * Add processor
     *
     * @param processor to add
     */
    void add(PunishmentProcessor processor);

    /**
     * Remove name of processor to remove
     * @param type to remove of
     * @param name to remove
     */
    void remove(PunishmentType type, String name);

    /**
     * Handle update
     * @param update to handle
     * @return should process anyway
     */
    boolean handle(Update update);
}
