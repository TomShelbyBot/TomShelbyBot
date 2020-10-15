package me.theseems.tomshel.pack;

import me.theseems.tomshel.Main;
import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.command.AdminRestricted;
import me.theseems.tomshel.command.SimpleCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import me.theseems.tomshel.punishment.Punishment;
import me.theseems.tomshel.punishment.PunishmentType;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ChatMember;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class UnmuteCommand extends SimpleCommand implements AdminRestricted {
  public UnmuteCommand() {
    super(
        SimpleCommandMeta.onLabel("unmute")
            .aliases("muteoff", "offmute", "pardon")
            .description("Размутить. Выдать.. Право.. Голоса"));
  }

  /**
   * Handle update for that command
   *
   * @param bot to handle with
   * @param update to handle
   */
  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    if (args.length == 0) {
      bot.sendBack(update, new SendMessage().setText("Укажите юзера кому нужно снять глушилку!"));
    } else {

      if (args[0].startsWith("@")) args[0] = args[0].substring(1);

      Optional<ChatMember> member =
          Main.getBot().getChatStorage().lookupMember(update.getMessage().getChatId(), args[0]);
      if (!member.isPresent()) {
        bot.sendBack(update, new SendMessage().setText("Не могу найти гражданина в сообщении."));
        return;
      }

      ChatMember actual = member.get();
      Optional<Punishment> punishmentOptional =
          bot.getPunishmentStorage()
              .getActivePunishment(actual.getUser().getId(), PunishmentType.MUTE);

      if (!punishmentOptional.isPresent()) {
        bot.sendBack(
            update,
            new SendMessage()
                .setText("У " + actual.getUser().getUserName() + " нет всунутых затычек")
                .setReplyToMessageId(update.getMessage().getMessageId()));
        return;
      }

      Punishment punishment = punishmentOptional.get();
      bot.getPunishmentStorage().removePunishment(actual.getUser().getId(), punishment);

      String reason =
          (punishment.getReason().isPresent()
              ? "'" + punishment.getReason().get() + "'"
              : "_<НЕ УКАЗАНА>_");

      bot.sendBack(
          update,
          new SendMessage()
              .setText(
                  update.getMessage().getFrom().getUserName()
                      + " размутил @"
                      + actual.getUser().getUserName()
                      + "\n\nЗаглушка была с надписью "
                      + reason)
              .setReplyToMessageId(update.getMessage().getMessageId())
              .enableMarkdown(true));
    }
  }
}
