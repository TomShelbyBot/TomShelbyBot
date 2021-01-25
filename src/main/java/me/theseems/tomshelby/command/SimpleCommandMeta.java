package me.theseems.tomshelby.command;

import java.util.*;

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
    if (aliases.length != 0) this.aliases.addAll(Arrays.asList(aliases));
    return this;
  }

  public SimpleCommandMeta explicitAccess() {
    this.accessExplicit = true;
    return this;
  }

  public SimpleCommandMeta explicitAccess(boolean accessExplicit) {
    this.accessExplicit = accessExplicit;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SimpleCommandMeta that = (SimpleCommandMeta) o;
    return accessExplicit == that.accessExplicit
        && Objects.equals(aliases, that.aliases)
        && label.equals(that.label)
        && Objects.equals(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(aliases, label, description, accessExplicit);
  }
}
