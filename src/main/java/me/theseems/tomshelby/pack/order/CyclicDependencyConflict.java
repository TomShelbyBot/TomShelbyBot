package me.theseems.tomshelby.pack.order;

import me.theseems.tomshelby.pack.BotPackageInfo;

public class CyclicDependencyConflict implements BotPackageConflict {
  private final BotPackageInfo from;
  private final BotPackageInfo to;

  public CyclicDependencyConflict(BotPackageInfo from, BotPackageInfo to) {
    this.from = from;
    this.to = to;
  }

  @Override
  public String getMessage() {
    return "Cyclic dependency: '" + from.getName() + "' <- (...) -> '" + to.getName() + "'";
  }

  @Override
  public String toString() {
    return getMessage();
  }
}
