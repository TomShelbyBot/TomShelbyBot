package me.theseems.tomshel.pack;

import com.google.common.base.Joiner;
import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.command.SimpleBotCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import me.theseems.tomshel.storage.TomMeta;
import me.theseems.tomshel.util.CommandUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class SummonBotCommand extends SimpleBotCommand {
  public static final String SUMMON_ACTIVE_MILS_KEY = "summonActiveMils";

  public SummonBotCommand() {
    super(
        new SimpleCommandMeta().label("summon").description("Отправить призыв прописать комманду"));
  }

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    if (args.length == 0) {
      bot.sendBack(update, new SendMessage().setText("Укажите команду которую нужно прописать!"));
      return;
    }

    String action = Joiner.on(' ').join(args);
    CommandUtils.CommandSkeleton skeleton = CommandUtils.extractCommand(action);
    if (!skeleton.success) {
      bot.sendBack(update, new SendMessage().setText("Не удалось распознать команду."));
      return;
    }

    KeyboardRow row = new KeyboardRow();
    row.add(action);

    TomMeta meta = bot.getChatStorage().getChatMeta(update.getMessage().getChatId());
    int summonActiveMils = meta.getInteger(SUMMON_ACTIVE_MILS_KEY).orElse(5000);

    bot.sendBack(
        update,
        new SendMessage()
            .setReplyMarkup(new ReplyKeyboardMarkup().setKeyboard(Collections.singletonList(row)))
            .setText(
                "Опаааа а тут просят вас это самое.. ну прописать комманду ("
                    + summonActiveMils / 1000
                    + " сек на реакцию) "
                    + action));

    Timer timer = new Timer();
    timer.schedule(
        new TimerTask() {
          @Override
          public void run() {
            bot.getCommandContainer()
                .get("unsummon")
                .ifPresent(command -> command.handle(bot, new String[] {}, update));
          }
        },
        summonActiveMils);
  }
}
