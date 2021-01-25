package me.theseems.tomshelby.poll;

import org.telegram.telegrambots.meta.api.objects.Update;

public class PollUpdate {
  private boolean isHandled;
  private final BotPoll botPoll;
  private final Update update;

  public PollUpdate(boolean isHandled, BotPoll botPoll, Update update) {
    this.isHandled = isHandled;
    this.botPoll = botPoll;
    this.update = update;
  }

  public boolean isHandled() {
    return isHandled;
  }

  public void setHandled(boolean handled) {
    isHandled = handled;
  }

  public BotPoll getPoll() {
    return botPoll;
  }

  public Update getUpdate() {
    return update;
  }
}
