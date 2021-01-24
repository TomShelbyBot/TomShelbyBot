package me.theseems.tomshelby.bootstrap;

import me.theseems.tomshelby.ThomasBot;
import org.slf4j.Logger;


public interface TargetBootstrap {
  void apply(Logger logger, ThomasBot bot);
}
