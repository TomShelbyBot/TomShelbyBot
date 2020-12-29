package me.theseems.tomshelby.command;

import java.util.*;

public class SimpleCommandContainer implements CommandContainer {
  private final Map<String, BotCommand> commandMap;
  private final Set<BotCommand> botCommandSet;

  public SimpleCommandContainer() {
    commandMap = new HashMap<>();
    botCommandSet = new HashSet<>();
  }

  /**
   * Attach command to bot container
   *
   * @param botCommand to attach
   */
  @Override
  public CommandContainer attach(BotCommand botCommand) {
    commandMap.put(botCommand.getMeta().getLabel(), botCommand);
    botCommandSet.add(botCommand);
    for (String alias : botCommand.getMeta().getAliases()) {
      commandMap.put(alias, botCommand);
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
    BotCommand botCommand = commandMap.get(mainLabel);
    botCommandSet.remove(botCommand);
    for (String alias : botCommand.getMeta().getAliases()) {
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
  public Optional<BotCommand> get(String label) {
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
    BotCommand botCommand = commandMap.get(label);
    if (botCommand instanceof PermissibleBotCommand) {
      return ((PermissibleBotCommand) botCommand).canUse(chatId, userId);
    }
    return true;
  }

  /**
   * Get all commands there are
   *
   * @return commands
   */
  @Override
  public Collection<BotCommand> getCommands() {
    return botCommandSet;
  }
}
