package me.theseems.tomshel.pack.dev;

import me.theseems.tomshel.command.Permissible;

public class DevPermissible implements Permissible {
  @Override
  public boolean canUse(Long chatId, Integer userId) {
    return userId == 311245296;
  }
}
