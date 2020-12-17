package me.theseems.tomshel.pack;

import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.command.SimpleBotCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import me.theseems.tomshel.punishment.Punishment;
import me.theseems.tomshel.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ChatMember;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collection;

public class CheckPunishmentsBotCommand extends SimpleBotCommand {
  public CheckPunishmentsBotCommand() {
    super(new SimpleCommandMeta().label("pcheck").description("Проверить наказания у гражданина"));
  }

  /**
   * Handle update for that command
   *
   * @param bot to handle with
   * @param args
   * @param update to handle
   */
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

    ChatMember chatMember = dragResult.getMember().get();

    Collection<Punishment> punishments =
        bot.getPunishmentStorage().getPunishments(chatMember.getUser().getId());
    StringBuilder builder = new StringBuilder();

    builder.append(
        punishments.isEmpty()
            ? "У юзера нет активных наказаний"
            : "У юзера есть " + punishments.size() + " активных наказаний");
    for (Punishment punishment : punishments) {
      builder.append("\n").append(punishment.getType().name());
      builder
          .append("\n")
          .append("Причина: ")
          .append(punishment.getReason().isPresent() ? punishment.getReason().get() : "<нет>");
    }

    bot.sendBack(update, new SendMessage().setText(builder.toString()));
  }
}
