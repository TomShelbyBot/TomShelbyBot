package me.theseems.tomshelby.bootstrap.builtin;

import me.theseems.tomshelby.Main;
import me.theseems.tomshelby.bootstrap.InitBootstrap;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MetaSaveBootstrap implements InitBootstrap {
  @Override
  public void apply(Logger logger) {
    // Enabling auto-save
    Executors.newScheduledThreadPool(1).scheduleAtFixedRate(Main::save, 10, 10, TimeUnit.SECONDS);
  }

  @Override
  public String getInitName() {
    return "Loading meta saver";
  }
}
