package me.theseems.tomshel.pack;

import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.command.SimpleBotCommand;
import me.theseems.tomshel.command.SimpleCommandMeta;
import me.theseems.tomshel.util.CommandUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Random;

public class RandomNumberBotCommand extends SimpleBotCommand {
  public RandomNumberBotCommand() {
    super(
        new SimpleCommandMeta()
            .label("random")
            .aliases("number", "randomit")
            .description("Получить рандомное число (диапазон тоже можно указать)."));
  }

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    int lowerBound = 1;
    int upperBound = 100;

    if (args.length == 1) {
      upperBound =
          CommandUtils.requirePositiveInt(
              args[0], "Укажите положительное число в качестве верхней границы для рандома!");
    } else if (args.length == 2) {
      lowerBound =
          CommandUtils.requirePositiveInt(
              args[0], "Укажите положительное число в качестве нижней границы для рандома!");
      upperBound =
          CommandUtils.requirePositiveInt(
              args[1], "Укажите положительное число в качестве верхней границы для рандома!");

      if (lowerBound > upperBound) {
        int temp = lowerBound;
        lowerBound = upperBound;
        upperBound = temp;
      }
    }

    if (upperBound > 10000) upperBound = 10000;
    int result = new Random().nextInt(upperBound - lowerBound + 1) + lowerBound;

    bot.sendBack(
        update,
        new SendMessage()
            .setReplyToMessageId(update.getMessage().getMessageId())
            .setText(
                "\n \nВыбираем число от "
                    + lowerBound
                    + " до "
                    + upperBound
                    + "\nВам выпало: *"
                    + result
                    + "*")
            .enableMarkdown(true));
  }
}
