package me.theseems.tomshelby.bootstrap;


import org.apache.logging.log4j.Logger;

public interface InitBootstrap {
  void apply(Logger logger);

  default String getInitName() {
    return getClass().getSimpleName();
  }
}
