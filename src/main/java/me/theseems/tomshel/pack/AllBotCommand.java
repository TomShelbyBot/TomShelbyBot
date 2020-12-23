package me.theseems.tomshel.pack;

import com.google.common.base.Joiner;
import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.command.AdminPermissibleBotCommand;
import me.theseems.tomshel.command.SimpleBotCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
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
