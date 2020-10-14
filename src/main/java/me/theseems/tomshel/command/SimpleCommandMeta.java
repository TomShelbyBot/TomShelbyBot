package me.theseems.tomshel.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SimpleCommandMeta implements CommandMeta {
  private final List<String> aliases;

  public SimpleCommandMeta(String... aliases) {
    this.aliases = new ArrayList<>(Arrays.asList(aliases));
  }

  public SimpleCommandMeta addAlias(String name) {
    aliases.add(name);
    return this;
  }

  /**
   * Get command's aliases
   *
   * @return aliases
   */
  @Override
  public Collection<String> getAliases() {
    return aliases;
  }
}
