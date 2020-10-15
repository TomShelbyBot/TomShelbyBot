package me.theseems.tomshel.punishment;

import me.theseems.tomshel.Main;
import me.theseems.tomshel.ThomasBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;

public class SimplePunishmentHandler implements PunishmentHandler {
  private final Map<PunishmentType, Collection<PunishmentProcessor>> processors;

  public SimplePunishmentHandler() {
    processors = new HashMap<>();
  }

  @Override
  public void add(PunishmentProcessor processor) {
    PunishmentType type = processor.getType();
    if (!processors.containsKey(type)) {
      processors.put(
          type,
          new PriorityBlockingQueue<>(
              2, Comparator.comparingInt(PunishmentProcessor::getPriority)));
    }

    processors.get(type).add(processor);
  }

  @Override
  public void remove(PunishmentType type, String name) {
    if (!processors.containsKey(type)) return;
    processors.get(type).removeIf(processor -> processor.getName().equals(name));
  }

  @Override
  public void handle(Update update) {
    System.out.println("Handling: ");
    System.out.println(
        Main.getBot()
            .getPunishmentStorage()
            .getPunishments(update.getMessage().getFrom().getId())
            .toString());
    for (Punishment punishment :
        Main.getBot()
            .getPunishmentStorage()
            .getPunishments(update.getMessage().getFrom().getId())) {
      System.out.println("Here we go! " + update);
      PunishmentType type = punishment.getType();
      if (!processors.containsKey(type)) continue;

      for (PunishmentProcessor punishmentProcessor : processors.get(type)) {
        System.out.println("Go for " + punishmentProcessor.toString());
        punishmentProcessor.handle(update, punishment);
      }
    }
  }
}
