package me.theseems.tomshelby.pack.order;

import me.theseems.tomshelby.pack.BotPackageInfo;

import java.util.Collection;

public interface BotPackageOrderManager {
  /**
   * Put input packages in a correct order
   * @param packages to order
   * @return order result
   */
    BotPackageOrderResult order(Collection<BotPackageInfo> packages);
}
