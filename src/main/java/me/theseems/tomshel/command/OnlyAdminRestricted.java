package me.theseems.tomshel.command;

import me.theseems.tomshel.Main;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface OnlyAdminRestricted extends Restricted {
  /**
   * Can a user use that command
   * @param userId to check
   * @return verdict
   */
  default boolean canUse(Long chatId, Integer userId) {
    try {
      return Main.getBot().execute(new GetChatAdministrators().setChatId(chatId)).stream()
          .anyMatch(chatMember -> chatMember.getUser().getId().equals(userId));
    } catch (TelegramApiException e) {
      return false;
    }
  }
}