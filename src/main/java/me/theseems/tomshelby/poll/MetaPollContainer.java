package me.theseems.tomshelby.poll;

import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.storage.TomMeta;

import java.util.Optional;

public class MetaPollContainer implements PollStorage {
  private static final String META_POLL_NAME = "polls";

  private String selfChatId;
  private ThomasBot thomasBot;

  public MetaPollContainer(ThomasBot thomasBot, String selfChatId) {
    this.thomasBot = thomasBot;
    this.selfChatId = selfChatId;
  }

  public MetaPollContainer() {}

  public void setSelfChatId(String selfChatId) {
    this.selfChatId = selfChatId;
  }

  public void setThomasBot(ThomasBot thomasBot) {
    this.thomasBot = thomasBot;
  }

  public TomMeta getPollsContainer() {
    return thomasBot.getChatStorage().getChatMeta(selfChatId).getOrCreateContainer(META_POLL_NAME);
  }

  public TomMeta getPollMeta(BotPoll botPoll) {
    return getPollsContainer().getOrCreateContainer(botPoll.getId());
  }

  /**
   * Put poll to the storage
   *
   * @param botPoll to put
   */
  @Override
  public void put(BotPoll botPoll) {
    getPollsContainer().set(botPoll.getId(), botPoll.getMeta());
  }

  /**
   * Remove poll from storage
   *
   * @param botPoll to remove
   */
  @Override
  public void remove(BotPoll botPoll) {
    getPollsContainer().remove(botPoll.getId());
  }

  /**
   * Get poll by it's identity
   *
   * @param pollId to get by
   * @return poll if found
   */
  @Override
  public Optional<BotPoll> getPoll(String pollId) {
    Optional<TomMeta> containerOptional =
        thomasBot.getChatStorage().getChatMeta(selfChatId).getContainer(META_POLL_NAME);

    return containerOptional.map(
        meta -> new MetaBotPoll(pollId, meta.getOrCreateContainer(pollId)));
  }
}
