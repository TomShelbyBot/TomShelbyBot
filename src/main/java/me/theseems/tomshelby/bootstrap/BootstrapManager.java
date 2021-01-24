package me.theseems.tomshelby.bootstrap;

import me.theseems.tomshelby.ThomasBot;
import org.slf4j.Logger;

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
      logger.info("Loading '" + initBootstrap.getClass().getSimpleName() + "'");
      initBootstrap.apply(logger);
    }
  }

  public void invokeTarget(Logger logger, ThomasBot bot) {
    for (TargetBootstrap initBootstrap : targetBootstrapSet) {
      logger.info("Targeting '" + initBootstrap.getClass().getSimpleName() + "'");
      initBootstrap.apply(logger, bot);
    }
  }
}
