package me.theseems.tomshelby.bootstrap;

import me.theseems.tomshelby.ThomasBot;
import org.apache.logging.log4j.Logger;


public interface TargetBootstrap {
  void apply(Logger logger, ThomasBot bot);

  default String getTargetName() {
    return getClass().getSimpleName();
  }
}
