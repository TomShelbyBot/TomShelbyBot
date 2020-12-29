package me.theseems.tomshelby.botcommands;

import com.google.common.base.Joiner;
import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.command.AdminPermissibleBotCommand;
import me.theseems.tomshelby.command.SimpleBotCommand;
import me.theseems.tomshelby.command.SimpleCommandMeta;
import me.theseems.tomshelby.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class BombBotCommand extends SimpleBotCommand implements AdminPermissibleBotCommand {
  public static final String COUNT_BOMB_MENTIONS_KEY = "bomb_mentions";

  public BombBotCommand() {
    super(new SimpleCommandMeta().label("bomb").description("Заспамить чатик упоминаниями юзера"));
  }

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    if (args.length == 0) {
      bot.replyBackText(
          update, "Укажите юзера аргументом. Также можно указать причину далее после юзера");
      return;
    }

    String userMention = args[0];
    if (!userMention.startsWith("@")) {
      userMention = "@" + userMention;
    }

    int countMentions =
        bot.getChatStorage()
            .getChatMeta(update.getMessage().getChatId())
            .getInteger(COUNT_BOMB_MENTIONS_KEY)
            .orElse(5);

    String reason = "";
    if (args.length > 1) {
      reason = " " + Joiner.on(' ').join(StringUtils.skipOne(args));
    }

    for (int i = 0; i < countMentions; i++) {
      bot.sendBack(update, new SendMessage().setText(userMention + reason));
    }
  }
}
