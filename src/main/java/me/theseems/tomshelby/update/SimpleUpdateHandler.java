package me.theseems.tomshelby.update;

import me.theseems.tomshelby.ThomasBot;

public abstract class SimpleUpdateHandler implements UpdateHandler {
  private int priority;

  public SimpleUpdateHandler() {
    priority = 0;
  }

  @Override
  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  public static void putConsecutively(ThomasBot bot, SimpleUpdateHandler... handlers) {
    int maxPriority =
        bot.getUpdateHandlerManager().getUpdateHandlers().stream()
            .max((o1, o2) -> Integer.compare(o2.getPriority(), o1.getPriority()))
            .map(UpdateHandler::getPriority)
            .orElse(0);

    for (int i = 0; i < handlers.length; i++) {
      handlers[i].setPriority(maxPriority + i + 1);
      bot.getUpdateHandlerManager().addUpdateHandler(handlers[i]);
    }
  }
}
