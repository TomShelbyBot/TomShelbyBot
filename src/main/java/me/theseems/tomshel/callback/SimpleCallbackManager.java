package me.theseems.tomshel.callback;

import me.theseems.tomshel.ThomasBot;
import me.theseems.tomshel.util.StringUtils;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;

public class SimpleCallbackManager implements CallbackManager {
  private final Map<String, Collection<CallbackHandler>> prefixMap;
  private final Map<String, CallbackHandler> nameMap;

  public SimpleCallbackManager() {
    prefixMap = new HashMap<>();
    nameMap = new HashMap<>();
  }

  /**
   * Add callback handler
   *
   * @param handler to add
   */
  @Override
  public void addCallbackHandler(CallbackHandler handler) {
    prefixMap.computeIfAbsent(handler.getPrefix(), s -> new ArrayList<>());
    prefixMap.get(handler.getPrefix()).add(handler);
  }

  /**
   * Remove callback handler
   *
   * @param handler to remove
   */
  @Override
  public void removeCallbackHandler(CallbackHandler handler) {
    if (!prefixMap.containsKey(handler.getPrefix())) return;
    if (!prefixMap.get(handler.getPrefix()).contains(handler)) return;

    prefixMap.get(handler.getPrefix()).remove(handler);
    if (prefixMap.get(handler.getPrefix()).size() == 0) prefixMap.remove(handler.getPrefix());

    nameMap.remove(handler.getName());
  }

  /**
   * Call the callback propagation
   *
   * @param bot to handle for
   * @param update to handle
   */
  @Override
  public void call(ThomasBot bot, Update update) {
    if (!update.hasCallbackQuery()) return;
    CallbackQuery query = update.getCallbackQuery();
    String data = query.getData();

    String[] args = data.split("#");
    if (args.length == 0) return;

    String prefix = args[0];
    if (!prefixMap.containsKey(prefix)) return;

    String[] finalArgs = StringUtils.skipOne(args);
    prefixMap.get(prefix).forEach(handler -> handler.onCallback(bot, update, finalArgs));
  }

  /**
   * Get handlers by prefix
   *
   * @param prefix to get by
   * @return handlers
   */
  @Override
  public Collection<CallbackHandler> getByPrefix(String prefix) {
    return prefixMap.get(prefix);
  }

  /**
   * Get all handlers there are in the manager
   *
   * @return handlers
   */
  @Override
  public Collection<CallbackHandler> getHandlers() {
    return nameMap.values();
  }

  /**
   * Get handler by name
   *
   * @param name to get by
   * @return handler if there is
   */
  @Override
  public Optional<CallbackHandler> getHandler(String name) {
    return Optional.ofNullable(nameMap.get(name));
  }
}
