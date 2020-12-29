package me.theseems.tomshelby.botcommands.dev.pack;

import me.theseems.tomshelby.Main;
import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.botcommands.dev.DevPermissibleBotCommand;
import me.theseems.tomshelby.command.SimpleBotCommand;
import me.theseems.tomshelby.command.SimpleCommandMeta;
import me.theseems.tomshelby.pack.BotPackage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class EnablePackBotCommand extends SimpleBotCommand implements DevPermissibleBotCommand {
  public EnablePackBotCommand() {
    super(SimpleCommandMeta.onLabel("enablepack").description("Включить пакет"));
  }

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    if (args.length == 0) {
      bot.replyBackText(update, "Укажите название пакета для включения");
      return;
    }

    String packageName = args[0];
    Optional<BotPackage> botPackage = bot.getPackageManager().getPackageByName(packageName);

    if (!botPackage.isPresent()) {
      bot.replyBackText(
          update, "Этого пакета нет среди доступных. Список можно получить написав /listpacks");
      return;
    }

    if (bot.getPackageManager().isEnabled(packageName)) {
      bot.replyBackText(update, "Этот пакет уже включен");
      return;
    }

    Main.getPackageManager().enablePackage(Main.getBot(), packageName);
    bot.replyBack(
        update,
        new SendMessage()
            .setText(
                ListPackBotCommand.getPackageEntry(bot.getPackageManager(), botPackage.get()))
                    .enableMarkdown(true));
  }
}
