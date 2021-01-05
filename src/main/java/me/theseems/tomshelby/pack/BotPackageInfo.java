package me.theseems.tomshelby.pack;

public interface BotPackageInfo {
  /**
   * Package name
   * @return name
   */
  String getName();

  /**
   * Get package author
   * @return author
   */
  String getAuthor();

  /**
   * Get package's version
   * @return version
   */
  String getVersion();

  /**
   * Get package description
   * @return package description
   */
  String getDescription();
}
