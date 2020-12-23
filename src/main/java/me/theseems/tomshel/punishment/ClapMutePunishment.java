package me.theseems.tomshel.punishment;

import java.time.temporal.ChronoUnit;

public class ClapMutePunishment extends TimedPunishment {
    public ClapMutePunishment(Integer period, ChronoUnit unit, String reason) {
        super(period, unit, reason);
    }

    @Override
    public PunishmentType getType() {
        return PunishmentType.CLAP_MUTE;
    }
}
