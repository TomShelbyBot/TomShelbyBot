package me.theseems.tomshelby.pack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BotPackageConfig implements BotPackageInfo {
  private String name;
  private String author;
  private String description;
  private String main;
  private String version;
  private List<String> dependencies = new ArrayList<>();

  public BotPackageConfig(
      String name, String author, String description, String main, String version) {
    this.name = name;
    this.author = author;
    this.description = description;
    this.main = main;
    this.version = version;
  }

  public BotPackageConfig(
      String name,
      String author,
      String description,
      String main,
      String version,
      List<String> dependencies) {
    this.name = name;
    this.author = author;
    this.description = description;
    this.main = main;
    this.version = version;
    this.dependencies = dependencies;
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  @Override
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getMain() {
    return main;
  }

  public void setMain(String main) {
    this.main = main;
  }

  @Override
  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public List<String> getDependencies() {
    return dependencies;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BotPackageConfig config = (BotPackageConfig) o;
    return name.equals(config.name)
        && author.equals(config.author)
        && Objects.equals(description, config.description)
        && main.equals(config.main)
        && version.equals(config.version)
        && Objects.equals(dependencies, config.dependencies);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, author, description, main, version, dependencies);
  }

  @Override
  public String toString() {
    return "BotPackageConfig{"
        + "name='"
        + name
        + '\''
        + ", author='"
        + author
        + '\''
        + ", description='"
        + description
        + '\''
        + ", main='"
        + main
        + '\''
        + ", version='"
        + version
        + '\''
        + ", dependencies="
        + dependencies
        + '}';
  }
}
