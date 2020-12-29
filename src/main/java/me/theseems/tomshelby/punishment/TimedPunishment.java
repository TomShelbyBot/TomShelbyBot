package me.theseems.tomshelby.punishment;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

public abstract class TimedPunishment implements Punishment {
  private final Instant validUntil;
  private final String reason;
  private PunishmentType type;

  public TimedPunishment(Instant validUntil, String reason) {
    this.validUntil = validUntil;
    this.reason = reason;
  }

  public TimedPunishment(Integer period, ChronoUnit unit, String reason) {
    this.validUntil = new Date().toInstant().plus(period, unit);
    this.reason = reason;
  }

  @Override
  public abstract PunishmentType getType();

  @Override
  public boolean isActive() {
    return validUntil.isAfter(new Date().toInstant());
  }

  @Override
  public Optional<String> getReason() {
    return Optional.ofNullable(reason);
  }
}
