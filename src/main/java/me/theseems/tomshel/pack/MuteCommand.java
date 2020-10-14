package me.theseems.tomshel.pack;

import com.google.common.base.Joiner;
import me.theseems.tomshel.Main;
import me.theseems.tomshel.TomasBot;
import me.theseems.tomshel.command.OnlyAdminRestricted;
import me.theseems.tomshel.command.SimpleCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import me.theseems.tomshel.punishment.MutePunishment;
import me.theseems.tomshel.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ChatMember;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class MuteCommand extends SimpleCommand implements OnlyAdminRestricted {
  public MuteCommand() {
    super(new SimpleCommandMeta().addAlias("мут"));
  }

  /**
   * Handle update for that command
   *
   * @param bot to handle with
   * @param update to handle
   */
  @Override
  public void handle(TomasBot bot, String[] args, Update update) {
    if (args.length == 0) {
      bot.sendBack(
          update, new SendMessage().setText("Укажите юзера кому нужно выдать пизды (мут)!"));
    } else {

      if (args[0].startsWith("@")) args[0] = args[0].substring(1);

      int period = 5;
      String reason = null;

      if (args.length != 1) {
        try {
          period = Integer.parseInt(args[1]);
          if (period <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
          bot.sendBack(
              update,
              new SendMessage().setText("Укажите нормальный срок (целое положительное число)"));
          return;
        }

        if (args.length > 2) {
          reason = Joiner.on(' ').join(StringUtils.skipOne(StringUtils.skipOne(args)));
        }
      }

      Optional<ChatMember> member =
          Main.getBot().getChatStorage().lookupMember(update.getMessage().getChatId(), args[0]);
      if (!member.isPresent()) {
        bot.sendBack(
            update,
            new SendMessage()
                .setText("Не могу найти юзера в сообщении. Проверьте ник.")
                .setReplyToMessageId(update.getMessage().getMessageId()));
        return;
      }

      ChatMember actual = member.get();
      Main.getBot()
          .getPunishmentStorage()
          .addPunishment(actual.getUser().getId(), new MutePunishment(period, reason));

      bot.sendBack(
          update,
          new SendMessage()
              .setText(
                  update.getMessage().getFrom().getUserName()
                      + " замутил @"
                      + actual.getUser().getUserName()
                      + (reason != null ? " по причине '" + reason + "'" : "")
                      + " на "
                      + period
                      + "с."));
    }
  }

  /**
   * Get label of the command
   *
   * @return label
   */
  @Override
  public String getLabel() {
    return "mute";
  }
}
