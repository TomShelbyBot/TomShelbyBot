package me.theseems.tomshelby.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SimpleCommandMeta implements CommandMeta {
  private final List<String> aliases;
  private String label;
  private String description;
  private boolean accessExplicit;

  public SimpleCommandMeta(String... aliases) {
    this.aliases = new ArrayList<>(Arrays.asList(aliases));
    this.accessExplicit = false;
  }

  public SimpleCommandMeta() {
    this(new String[0]);
  }

  public static SimpleCommandMeta onLabel(String label) {
    return new SimpleCommandMeta().label(label);
  }

  public SimpleCommandMeta label(String label) {
    this.label = label;
    return this;
  }

  public SimpleCommandMeta description(String description) {
    this.description = description;
    return this;
  }

  public SimpleCommandMeta alias(String name) {
    aliases.add(name);
    return this;
  }

  public SimpleCommandMeta aliases(String... aliases) {
    this.aliases.addAll(Arrays.asList(aliases));
    return this;
  }

  public SimpleCommandMeta explicitAccess() {
    this.accessExplicit = true;
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

  /**
   * Get command's label
   *
   * @return label
   */
  @Override
  public String getLabel() {
    return label;
  }

  /**
   * Get description
   *
   * @return description
   */
  @Override
  public String getDescription() {
    return description;
  }

  /**
   * Should we react only to /label@bot_username
   *
   * @return explicit access requirement
   */
  public boolean isAccessExplicit() {
    return accessExplicit;
  }
}
