package me.theseems.tomshel.pack;

import com.google.common.base.Joiner;
import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.command.AdminPermissibleBotCommand;
import me.theseems.tomshel.command.SimpleBotCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import me.theseems.tomshel.punishment.ClapMutePunishment;
import me.theseems.tomshel.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ChatMember;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.temporal.ChronoUnit;

public class ClapMuteBotCommand extends SimpleBotCommand implements AdminPermissibleBotCommand {

  public ClapMuteBotCommand() {
    super(
        SimpleCommandMeta.onLabel("gag")
            .aliases("кляп", "clapmute", "muteclap")
            .description("Вставить кляп. Именно кляп! Особый вид мута"));
  }

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    StringUtils.DragResult dragResult = StringUtils.dragFrom(update, args);
    if (!dragResult.getMember().isPresent()) {
      bot.sendBack(
          update,
          new SendMessage()
              .setText("Не могу найти гражданина которому нужно вставить кляп!")
              .setReplyToMessageId(update.getMessage().getMessageId()));
      return;
    }

    if (dragResult.isSkipArg()) args = StringUtils.skipOne(args);

    if (args.length == 0) {
      bot.sendBack(
          update,
          new SendMessage()
              .setText(
                  "Укажите нормальный срок присутствия кляпа во рту! (Целое положительно число длиной меньше 10)")
              .setReplyToMessageId(update.getMessage().getMessageId()));
      return;
    }

    int period;
    try {
      period = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      bot.sendBack(
          update,
          new SendMessage()
              .setText(
                  "Укажите нормальный срок присутствия кляпа во рту! (Целое положительно число длиной меньше 10)")
              .setReplyToMessageId(update.getMessage().getMessageId()));
      return;
    }

    ChatMember chatMember = dragResult.getMember().get();
    bot.getPunishmentStorage()
        .addPunishment(
            chatMember.getUser().getId(),
            new ClapMutePunishment(
                period, ChronoUnit.SECONDS, Joiner.on(' ').join(StringUtils.skipOne(args))));

    bot.sendBack(
        update,
        new SendMessage()
            .setText(
                update.getMessage().getFrom().getUserName()
                    + " вставил кляп в рот @"
                    + chatMember.getUser().getUserName()
                    + " на "
                    + period
                    + "c."
                    + (args.length > 1
                        ? " со словами '" + Joiner.on(' ').join(StringUtils.skipOne(args)) + "'"
                        : " При этом ничего не сказал...")));
  }
}
