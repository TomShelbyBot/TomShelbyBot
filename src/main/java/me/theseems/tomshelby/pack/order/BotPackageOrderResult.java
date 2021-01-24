package me.theseems.tomshelby.pack.order;

import me.theseems.tomshelby.pack.BotPackageInfo;

import java.util.Collection;

public class BotPackageOrderResult {
  private Collection<BotPackageInfo> orderedPackages;
  private Collection<BotPackageConflict> conflicts;

  public BotPackageOrderResult(
      Collection<BotPackageInfo> orderedPackages, Collection<BotPackageConflict> conflicts) {
    this.orderedPackages = orderedPackages;
    this.conflicts = conflicts;
  }

  public BotPackageOrderResult() {}

  public Collection<BotPackageConflict> getConflicts() {
    return conflicts;
  }

  public Collection<BotPackageInfo> getOrderedPackages() {
    return orderedPackages;
  }

  public void setOrderedPackages(Collection<BotPackageInfo> orderedPackages) {
    this.orderedPackages = orderedPackages;
  }

  public void setConflicts(Collection<BotPackageConflict> conflicts) {
    this.conflicts = conflicts;
  }

  @Override
  public String toString() {
    return "BotPackageOrderResult{"
        + "orderedPackages="
        + orderedPackages
        + ", conflicts="
        + conflicts
        + '}';
  }
}
