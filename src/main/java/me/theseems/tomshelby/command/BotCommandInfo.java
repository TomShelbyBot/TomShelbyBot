package me.theseems.tomshelby.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface BotCommandInfo {
  String label();

  String[] aliases() default {};

  boolean explicitAccess() default false;

  String description() default "";
}
