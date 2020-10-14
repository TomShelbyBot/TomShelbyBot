package me.theseems.tomshel.command;

import java.util.Collection;
import java.util.Optional;

public interface CommandContainer {
  /**
   * Attach command to bot container
   * @param command to attach
   */
  CommandContainer attach(Command command);

  /**
   * Detach command from container
   * @param mainLabel of the command to remove
   */
  void detach(String mainLabel);

  /**
   * Get command by it's label
   * @param label to get
   * @return command if is found
   */
  Optional<Command> get(String label);

  /**
   * Is command accessible
   * @param label of command
   * @param userId executor
   * @return accessible
   */
  boolean isAccessible(String label, Long chatId, Integer userId);

  /**
   * Get all commands there are
   * @return commands
   */
  Collection<Command> getCommands();
}
