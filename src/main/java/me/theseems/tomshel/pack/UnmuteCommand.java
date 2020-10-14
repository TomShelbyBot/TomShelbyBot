package me.theseems.tomshel.pack;

import me.theseems.tomshel.Main;
import me.theseems.tomshel.TomasBot;
import me.theseems.tomshel.command.OnlyAdminRestricted;
import me.theseems.tomshel.command.SimpleCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import me.theseems.tomshel.punishment.Punishment;
import me.theseems.tomshel.punishment.PunishmentType;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ChatMember;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class UnmuteCommand extends SimpleCommand implements OnlyAdminRestricted {
  public UnmuteCommand() {
    super(new SimpleCommandMeta().addAlias("анмут"));
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
          update, new SendMessage().setText("Укажите юзера кому нужно отнять пизды (мут)!"));
    } else {

      if (args[0].startsWith("@")) args[0] = args[0].substring(1);

      Optional<ChatMember> member =
          Main.getBot().getChatStorage().lookupMember(update.getMessage().getChatId(), args[0]);
      if (!member.isPresent()) {
        bot.sendBack(
            update,
            new SendMessage()
                .setText(
                    "Не могу найти юзера в сообщении. Проверьте ник или укажите его без собачки."));
        return;
      }

      ChatMember actual = member.get();
      Optional<Punishment> punishmentOptional =
          bot.getPunishmentStorage()
              .getActivePunishment(actual.getUser().getId(), PunishmentType.MUTED);

      if (!punishmentOptional.isPresent()) {
        bot.sendBack(
            update,
            new SendMessage()
                .setText("У " + actual.getUser().getUserName() + " нет активных мутов")
                .setReplyToMessageId(update.getMessage().getMessageId()));
        return;
      }

      Punishment punishment = punishmentOptional.get();
      bot.getPunishmentStorage().removePunishment(actual.getUser().getId(), punishment);

      bot.sendBack(
          update,
          new SendMessage()
              .setText(
                  update.getMessage().getFrom().getUserName()
                      + " размутил @"
                      + actual.getUser().getUserName()
                      + "\n\nНаказание было выдано по причине "
                      + (punishment.getReason().isPresent()
                          ? punishment.getReason().get()
                          : "_<НЕ УКАЗАНА>_"))
              .setReplyToMessageId(update.getMessage().getMessageId())
              .enableMarkdown(true));
    }
  }

  /**
   * Get label of the command
   *
   * @return label
   */
  @Override
  public String getLabel() {
    return "unmute";
  }
}
