package me.theseems.tomshel.pack;

import com.google.common.base.Joiner;
import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.command.SimpleBotCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import me.theseems.tomshel.util.CommandUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Collections;

public class SummonBotCommand extends SimpleBotCommand {

  public SummonBotCommand() {
    super(
        new SimpleCommandMeta().label("summon").description("Отправить призыв прописать комманду"));
  }

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    if (args.length == 0) {
      bot.sendBack(update, new SendMessage().setText("Укажите комманду которую нужно прописать!"));
      return;
    }

    String action = Joiner.on(' ').join(args);
    CommandUtils.CommandSkeleton skeleton = CommandUtils.extractCommand(action);
    if (!skeleton.success) {
      bot.sendBack(update, new SendMessage().setText("Не удалось распознать комманду."));
      return;
    }

    KeyboardRow row = new KeyboardRow();
    row.add(action);

    bot.sendBack(
        update,
        new SendMessage()
            .setReplyMarkup(new ReplyKeyboardMarkup().setKeyboard(Collections.singletonList(row)))
            .setText("Опаааа а тут просят вас это самое.. ну прописать комманду " + action));
  }
}
