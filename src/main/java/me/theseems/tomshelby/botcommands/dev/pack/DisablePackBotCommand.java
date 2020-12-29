package me.theseems.tomshelby.botcommands.dev.pack;

import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.botcommands.dev.DevPermissibleBotCommand;
import me.theseems.tomshelby.command.SimpleBotCommand;
import me.theseems.tomshelby.command.SimpleCommandMeta;
import me.theseems.tomshelby.pack.BotPackage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class DisablePackBotCommand extends SimpleBotCommand implements DevPermissibleBotCommand {
  public DisablePackBotCommand() {
    super(SimpleCommandMeta.onLabel("disablepack").description("Выключить пакет"));
  }

  @Override
  public void handle(ThomasBot bot, String[] args, Update update) {
    if (args.length == 0) {
      bot.replyBackText(update, "Укажите название пакета для выключения");
      return;
    }

    String packageName = args[0];
    Optional<BotPackage> botPackage = bot.getPackageManager().getPackageByName(packageName);

    if (!botPackage.isPresent()) {
      bot.replyBackText(
          update, "Этого пакета нет среди доступных. Список можно получить написав /listpacks");
      return;
    }

    if (!bot.getPackageManager().isEnabled(packageName)) {
      bot.replyBackText(update, "Этот пакет уже выключен");
      return;
    }

    try {
      bot.getPackageManager().disablePackage(packageName);
    } catch (Exception e) {
      System.err.println("Error disabling package '" + packageName + "'");
      bot.replyBackText(update, "К сожалению, не удалось правильно выключить пак...");
      bot.replyBackText(update, e.getMessage());
      e.printStackTrace();
    }

    bot.replyBack(
        update,
        new SendMessage()
            .setText(ListPackBotCommand.getPackageEntry(bot.getPackageManager(), botPackage.get()))
            .enableMarkdown(true));
  }
}
