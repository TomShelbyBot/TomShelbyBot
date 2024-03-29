package me.theseems.tomshelby.command;

import me.theseems.tomshelby.Main;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface AdminPermissibleBotCommand extends PermissibleBotCommand {
  /**
   * Can a user use that command
   *
   * @param userId to check
   * @return verdict
   */
  default boolean canUse(String chatId, Long userId) {
    try {
      GetChatAdministrators getChatAdministrators = new GetChatAdministrators();
      getChatAdministrators.setChatId(chatId);

      return Main.getBot().execute(getChatAdministrators).stream()
          .anyMatch(chatMember -> chatMember.getUser().getId().equals(userId));
    } catch (TelegramApiException e) {
      return false;
    }
  }
}
