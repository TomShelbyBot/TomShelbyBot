package me.theseems.tomshel.pack;

import com.google.common.base.Joiner;
import me.theseems.tomshel.TomasBot;
import me.theseems.tomshel.command.SimpleCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StraponCommand extends SimpleCommand {
  public StraponCommand() {
    super(new SimpleCommandMeta().addAlias("страпон").addAlias("straponik"));
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
          update,
          new SendMessage()
              .setText("Укажи кого нужно отстрапонить")
              .setReplyToMessageId(update.getMessage().getMessageId()));
    } else {
      bot.sendBack(
          update,
          new SendMessage()
              .setText(
                  "__ "
                      + "@"
                      + update.getMessage().getFrom().getUserName()
                      + " отстрапонил "
                      + args[0]
                      + " "
                      + Joiner.on(' ')
                          .join(Arrays.stream(args).skip(1).collect(Collectors.toList()))
                      + " __")
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
    return "strap";
  }
}
