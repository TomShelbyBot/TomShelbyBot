package me.theseems.tomshelby.pack;

public class BotPackageConfig implements BotPackageInfo {
  private String name;
  private String author;
  private String description;
  private String main;
  private String version;

  public BotPackageConfig(
      String name, String author, String description, String main, String version) {
    this.name = name;
    this.author = author;
    this.description = description;
    this.main = main;
    this.version = version;
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
        + '}';
  }
}
