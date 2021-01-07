package me.theseems.tomshelby.pack.order;

import me.theseems.tomshelby.pack.BotPackageInfo;

public class DependencyNotFoundConflict implements BotPackageConflict {
  private final String dependency;
  private final BotPackageInfo packageInfo;

  public DependencyNotFoundConflict(String dependency, BotPackageInfo packageInfo) {
    this.dependency = dependency;
    this.packageInfo = packageInfo;
  }

  @Override
  public String getMessage() {
    return "Dependency '"
        + dependency
        + "' was not found for package '"
        + packageInfo.getName()
        + "'";
  }

  @Override
  public String toString() {
    return getMessage();
  }
}
