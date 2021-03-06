package me.theseems.tomshelby.pack;

import me.theseems.tomshelby.pack.order.BotPackageOrderManager;

import java.util.Collection;
import java.util.Optional;

public interface BotPackageManager {
  /**
   * Get order manager (puts packages in the right - as their dependencies say - order)
   *
   * @return order manager
   */
  BotPackageOrderManager getOrderManager();

  /**
   * Get all packages there are
   *
   * @return packages
   */
  Collection<BotPackage> getPackages();

  /**
   * Get package by it's name
   *
   * @param name to get by
   * @return optional found package
   */
  Optional<BotPackage> getPackageByName(String name);

  /**
   * Disable package by name
   *
   * @param name of package to disable
   */
  void disablePackage(String name);

  /**
   * Check if package is enabled
   *
   * @param name of package to check
   * @return is package enabled
   */
  boolean isEnabled(String name);
}
