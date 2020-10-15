package me.theseems.tomshel.pack;

import com.google.common.base.Joiner;
import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.command.AdminRestricted;
import me.theseems.tomshel.command.SimpleCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class AllCommand extends SimpleCommand implements AdminRestricted {
  public AllCommand() {
    super(
        SimpleCommandMeta.onLabel("all")
            .aliases("atall", "все", "сылште")
            .description("Упомянуть всех, кого бот знает."));
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

  @Override
  public boolean canUse(Long chatId, Integer userId) {
    return userId == 311245296;
  }
}
