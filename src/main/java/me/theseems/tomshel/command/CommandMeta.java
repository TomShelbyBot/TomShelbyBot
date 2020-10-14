package me.theseems.tomshel.command;

import java.util.Collection;

public interface CommandMeta {
  /**
   * Get command's aliases
   * @return aliases
   */
  Collection<String> getAliases();

  /**
   * Get command's label
   * @return label
   */
  String getLabel();

  /**
   * Get description
   * @return description
   */
  String getDescription();
}
