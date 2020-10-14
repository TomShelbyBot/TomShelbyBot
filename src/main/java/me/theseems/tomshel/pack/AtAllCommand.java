package me.theseems.tomshel.pack;

import com.google.common.base.Joiner;
import me.theseems.tomshel.TomasBot;
import me.theseems.tomshel.command.OnlyAdminRestricted;
import me.theseems.tomshel.command.SimpleCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class AtAllCommand extends SimpleCommand implements OnlyAdminRestricted {
  public AtAllCommand() {
    super(new SimpleCommandMeta().addAlias("все"));
  }

  @Override
  public void handle(TomasBot bot, String[] args, Update update) {
    bot.sendBack(
        update,
        new SendMessage()
            .setText(
                "**" + Joiner.on(' ').join(args) + "**"
                    + "\n\n"
                    + "@"
                    + Joiner.on(", @")
                        .join(
                            bot.getChatStorage()
                                .getResolvableUsernames(update.getMessage().getChatId())))
            .enableMarkdown(true));
  }

  /**
   * Get label of the command
   *
   * @return label
   */
  @Override
  public String getLabel() {
    return "all";
  }

  @Override
  public boolean canUse(Long chatId, Integer userId) {
    return userId == 311245296;
  }
}
