package me.theseems.tomshel.punishment;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

public class MutePunishment implements Punishment {
  private final Instant validUntil;
  private final String reason;

  public MutePunishment(int period, String reason) {
    validUntil = new Date().toInstant().plus(period, ChronoUnit.SECONDS);
    this.reason = reason;
  }

  /**
   * Get type of punishment
   *
   * @return type
   */
  @Override
  public PunishmentType getType() {
    return PunishmentType.MUTED;
  }

  /**
   * Is punishment active
   *
   * @return is active
   */
  @Override
  public boolean isActive() {
    return validUntil.isAfter(new Date().toInstant());
  }

  /**
   * Get reason of punishment
   *
   * @return reason
   */
  @Override
  public Optional<String> getReason() {
    return Optional.ofNullable(reason);
  }
}
