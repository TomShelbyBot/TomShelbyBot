package me.theseems.tomshel.punishment;

import me.theseems.tomshel.Main;
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
              1, Comparator.comparingInt(PunishmentProcessor::getPriority)));
    }

    processors.get(type).add(processor);
  }

  @Override
  public void remove(PunishmentType type, String name) {
    if (!processors.containsKey(type)) return;
    processors.get(type).removeIf(processor -> processor.getName().equals(name));
  }

  @Override
  public boolean handle(Update update) {
    Integer fromId = null;

    if (!update.hasMessage()) {
      if (update.hasPollAnswer() && update.getPollAnswer().getUser() != null) {
        fromId = update.getPollAnswer().getUser().getId();
      }
    } else {
      fromId = update.getMessage().getFrom().getId();
    }

    if (fromId == null) return false;

    for (Punishment punishment :
        Main.getBot()
            .getPunishmentStorage()
            .getPunishments(fromId)) {

      PunishmentType type = punishment.getType();
      if (!processors.containsKey(type)) continue;

      for (PunishmentProcessor punishmentProcessor : processors.get(type)) {
        boolean verdict = punishmentProcessor.handle(update, punishment);
        if (!verdict) return false;
      }
    }

    return true;
  }
}
