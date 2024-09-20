package ru.collapsedev.collapseapi.common.object;

import org.bukkit.entity.Player;
import ru.collapsedev.collapseapi.api.action.Action;
import ru.collapsedev.collapseapi.api.menu.action.MenuAction;
import ru.collapsedev.collapseapi.builder.ParticleBuilder;
import ru.collapsedev.collapseapi.util.BukkitUtil;
import ru.collapsedev.collapseapi.util.PlayerUtil;
import ru.collapsedev.collapseapi.util.StringUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultActions {

    private static final Map<String, Action> actions = Map.of(
            "[command]", BukkitUtil::executeCommandPlaceholdered,
            "[sound]", PlayerUtil::playSound,
            "[message]", PlayerUtil::sendMessage,
            "[broadcast]", (player, quote) -> PlayerUtil.broadcast(StringUtil.applyPlaceholders(player, quote)),
            "[exit]", (player, quote) -> player.closeInventory(),
            "[player]", PlayerUtil::sendCommand,
            "[title]", PlayerUtil::sendTitle,
            "[actionbar]", PlayerUtil::sendActionBar,
            "[particle]", (player, quote) -> new ParticleBuilder(quote, player.getLocation().clone()).spawn()
    );

    public static Map<String, MenuAction> getMenuActions() {
        return actions.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        map -> map.getValue().toMenuAction()
                ));
    }

    public static void execute(Player player, List<String> actions) {
        DefaultActions.actions.forEach((pattern, action)
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
