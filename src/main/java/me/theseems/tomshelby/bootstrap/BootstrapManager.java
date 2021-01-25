package me.theseems.tomshelby.bootstrap;

import me.theseems.tomshelby.ThomasBot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class BootstrapManager {
  private final List<InitBootstrap> initBootstrapSet;
  private final List<TargetBootstrap> targetBootstrapSet;

  public BootstrapManager() {
    initBootstrapSet = new ArrayList<>();
    targetBootstrapSet = new ArrayList<>();
  }

  public BootstrapManager init(InitBootstrap initBootstrap) {
    this.initBootstrapSet.add(initBootstrap);
    return this;
  }

  public BootstrapManager target(TargetBootstrap targetBootstrap) {
    this.targetBootstrapSet.add(targetBootstrap);
    return this;
  }

  public void invokeInit(Logger logger) {
    for (InitBootstrap initBootstrap : initBootstrapSet) {
      logger.debug("Init stage> " + initBootstrap.getInitName());
      initBootstrap.apply(LogManager.getLogger(initBootstrap.getInitName()));
    }
    logger.debug("Init finished");
  }

  public void invokeTarget(org.apache.logging.log4j.Logger logger, ThomasBot bot) {
    for (TargetBootstrap initBootstrap : targetBootstrapSet) {
      logger.debug("Target stage> " + initBootstrap.getTargetName());
      initBootstrap.apply(LogManager.getLogger(initBootstrap.getTargetName()), bot);
    }
    logger.debug("Target finished");
  }
}
