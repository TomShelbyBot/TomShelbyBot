package me.theseems.tomshelby.command;

import me.theseems.tomshelby.ThomasBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

public abstract class SimpleBotCommand implements BotCommand {
  private final CommandMeta meta;

  public SimpleBotCommand(CommandMeta meta) {
    this.meta = meta;
  }

  public SimpleBotCommand() {
    // Process annotation
    Class<? extends SimpleBotCommand> clazz = getClass();
    Command info = clazz.getAnnotation(Command.class);
    this.meta =
        SimpleCommandMeta.onLabel(info.label())
            .description(info.description())
            .aliases(info.aliases())
            .explicitAccess(info.explicitAccess());
  }

  /**
   * Handle update for that command
   *
   * @param bot to handle with
   * @param update to handle
   */
  @Override
  public abstract void handle(ThomasBot bot, String[] args, Update update);

  /**
   * Get meta of the command
   *
   * @return meta
   */
  @Override
  public CommandMeta getMeta() {
    return meta;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SimpleBotCommand that = (SimpleBotCommand) o;
    return Objects.equals(meta, that.meta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(meta);
  }
}
