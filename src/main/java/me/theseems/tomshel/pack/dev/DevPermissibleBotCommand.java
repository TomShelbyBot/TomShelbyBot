package me.theseems.tomshel.pack.dev;

import me.theseems.tomshel.command.PermissibleBotCommand;

public interface DevPermissibleBotCommand extends PermissibleBotCommand {
  @Override
  default boolean canUse(Long chatId, Integer userId) {
    return userId == 311245296; // Temp. Only for @theseems
  }
}
