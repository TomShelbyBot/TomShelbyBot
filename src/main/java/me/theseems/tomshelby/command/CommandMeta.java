package me.theseems.tomshelby.command;

import java.util.Collection;

public interface CommandMeta {
  /**
   * Get command's aliases
   *
   * @return aliases
   */
  Collection<String> getAliases();

  /**
   * Get command's label
   *
   * @return label
   */
  String getLabel();

  /**
   * Get description
   *
   * @return description
   */
  String getDescription();

  /**
   * Should we react only to /label@bot_username
   *
   * @return explicit access requirement
   */
  boolean isAccessExplicit();
}
