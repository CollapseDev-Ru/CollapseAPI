package ru.collapsedev.collapseapi.common.object;

import lombok.Getter;
import org.bukkit.entity.Player;
import ru.collapsedev.collapseapi.util.BukkitUtil;
import ru.collapsedev.collapseapi.util.PlayerUtil;
import ru.collapsedev.collapseapi.util.StringUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Actions {
    public interface IActions {
        void onAction(Player player, String quote);
    }

    @Getter
    private static final Map<String, IActions> actions = Map.of(
            "[command]", (BukkitUtil::executeCommandPlaceholdered),
            "[sound]", PlayerUtil::playSound,
            "[message]", PlayerUtil::sendMessage,
            "[broadcast]", (player, quote) -> PlayerUtil.broadcast(StringUtil.applyPlaceholders(player, quote)),
            "[exit]", (player, quote) -> player.closeInventory(),
            "[player]", PlayerUtil::sendCommand,
            "[title]", PlayerUtil::sendTitle,
            "[actionbar]", PlayerUtil::sendActionBar
    );

    public static void use(List<String> actions, Player player) {
        Actions.actions.forEach((pattern, action)
                -> getQuotes(pattern, actions).forEach(quote
                -> action.onAction(player, quote)));
    }

    public static List<String> getQuotes(String actionPattern, List<String> actions) {
        return actions.stream()
                .filter(action -> action.startsWith(actionPattern))
                .map(action -> {
                    String cleanAction = action.substring(actionPattern.length()).trim();
                    return cleanAction.isEmpty() ? null : cleanAction;
                })
                .collect(Collectors.toList());
    }

}
