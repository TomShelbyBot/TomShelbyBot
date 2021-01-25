package me.theseems.tomshelby.bootstrap.builtin;

import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.bootstrap.TargetBootstrap;
import me.theseems.tomshelby.pack.BotPackage;
import me.theseems.tomshelby.pack.BotPackageInfo;
import me.theseems.tomshelby.pack.order.BotPackageOrderResult;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

public class EnablePackagesBootstrap implements TargetBootstrap {
  @Override
  public void apply(Logger logger, ThomasBot bot) {
    BotPackageOrderResult result =
        bot.getPackageManager()
            .getOrderManager()
            .order(
                bot.getPackageManager().getPackages().stream()
                    .map(BotPackage::getInfo)
                    .collect(Collectors.toList()));

    for (BotPackageInfo pack : result.getOrderedPackages()) {
      logger.info(
          "Enabling pack '"
              + pack.getName()
              + "' v"
              + pack.getVersion()
              + " by "
              + pack.getAuthor());
      LoadPackagesBootstrap.getJarBotPackageManager().enablePackage(bot, pack.getName());
    }
  }

  @Override
  public String getTargetName() {
    return "Enabling packages";
  }
}
