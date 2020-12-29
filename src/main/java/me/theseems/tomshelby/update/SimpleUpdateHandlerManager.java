package me.theseems.tomshelby.update;

import me.theseems.tomshelby.ThomasBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.ConcurrentSkipListSet;

public class SimpleUpdateHandlerManager implements UpdateHandlerManager {
  private final ConcurrentSkipListSet<UpdateHandler> updateHandlers;

  public SimpleUpdateHandlerManager() {
    updateHandlers =
        new ConcurrentSkipListSet<>(Comparator.comparingInt(UpdateHandler::getPriority));
  }

  @Override
  public void addUpdateHandler(UpdateHandler handler) {
    updateHandlers.add(handler);
  }

  @Override
  public void removeUpdateHandler(UpdateHandler handler) {
    updateHandlers.remove(handler);
  }

  @Override
  public boolean handleUpdate(ThomasBot bot, Update update) {
    for (UpdateHandler updateHandler : updateHandlers) {
      if (!updateHandler.handleUpdate(bot, update)) return false;
    }
    return true;
  }

  @Override
  public Collection<UpdateHandler> getUpdateHandlers() {
    return updateHandlers;
  }
}
