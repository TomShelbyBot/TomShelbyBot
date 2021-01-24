package me.theseems.tomshelby.pack;

import java.util.Collections;
import java.util.List;

public interface BotPackageInfo {
  /**
   * Package name
   *
   * @return name
   */
  String getName();

  /**
   * Get package author
   *
   * @return author
   */
  String getAuthor();

  /**
   * Get package's version
   *
   * @return version
   */
  String getVersion();

  /**
   * Get package description
   *
   * @return package description
   */
  String getDescription();

  /**
   * Get package's dependencies
   *
   * @return list of dependencies
   */
  default List<String> getDependencies() {
    return Collections.emptyList();
  }
}
