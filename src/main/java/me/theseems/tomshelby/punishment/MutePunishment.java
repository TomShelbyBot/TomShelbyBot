package me.theseems.tomshelby.punishment;

import java.time.temporal.ChronoUnit;

public class MutePunishment extends TimedPunishment {
  public MutePunishment(Integer period, ChronoUnit unit, String reason) {
    super(period, unit, reason);
  }

  @Override
  public PunishmentType getType() {
    return PunishmentType.MUTE;
  }
}
