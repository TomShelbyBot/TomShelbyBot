package me.theseems.tomshelby.botcommands;

import com.google.common.base.Joiner;
import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.command.AdminPermissibleBotCommand;
import me.theseems.tomshelby.command.SimpleBotCommand;
import me.theseems.tomshelby.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class AllBotCommand extends SimpleBotCommand implements AdminPermissibleBotCommand {
  public AllBotCommand() {
    super(SimpleCommandMeta.onLabel("all").description("Упомянуть всех, кого бот знает."));
  }

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    bot.sendBack(
        update,
        new SendMessage()
            .setText(
                "**"
                    + Joiner.on(' ').join(args)
                    + "**"
                    + "\n\n"
                    + "@"
                    + Joiner.on(", @")
                        .join(
                            bot.getChatStorage()
                                .getResolvableUsernames(update.getMessage().getChatId())))
            .enableMarkdown(true));
  }
}
