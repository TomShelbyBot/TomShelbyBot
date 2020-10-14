package me.theseems.tomshel.command;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SimpleCommandContainer implements CommandContainer {
  private final Map<String, Command> commandMap;

  public SimpleCommandContainer() {
    commandMap = new HashMap<>();
  }

  /**
   * Attach command to bot container
   *
   * @param command to attach
   */
  @Override
  public CommandContainer attach(Command command) {
    commandMap.put(command.getMeta().getLabel(), command);
    for (String alias : command.getMeta().getAliases()) {
      commandMap.put(alias, command);
    }
    return this;
  }

  /**
   * Detach command from container
   *
   * @param mainLabel of the command to remove
   */
  @Override
  public void detach(String mainLabel) {
    Command command = commandMap.get(mainLabel);
    for (String alias : command.getMeta().getAliases()) {
      commandMap.remove(alias);
    }
  }

  /**
   * Get command by it's label
   *
   * @param label to get
   * @return command if is found
   */
  @Override
  public Optional<Command> get(String label) {
    return Optional.ofNullable(commandMap.get(label));
  }

  /**
   * Is command accessible
   *
   * @param label  of command
   * @param userId executor
   * @return accessible
   */
  @Override
  public boolean isAccessible(String label, Long chatId, Integer userId) {
    if (!commandMap.containsKey(label)) return true;
    Command command = commandMap.get(label);
    if (command instanceof Restricted) {
      return ((Restricted) command).canUse(chatId, userId);
    }
    return true;
  }

  /**
   * Get all commands there are
   *
   * @return commands
   */
  @Override
  public Collection<Command> getCommands() {
    Map<String, Command> commandMap = this.commandMap;
    commandMap.forEach(
        (s, command) -> {
          for (String alias : command.getMeta().getAliases()) {
            commandMap.remove(alias);
          }
        });
    return commandMap.values();
  }
}
