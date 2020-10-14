package me.theseems.tomshel.command;

import java.util.Collection;

public interface CommandMeta {
  /**
   * Get command's aliases
   * @return aliases
   */
  Collection<String> getAliases();
}
