package ru.collapsedev.collapseapi.common.object;

import ru.collapsedev.collapseapi.api.menu.Menu;
import ru.collapsedev.collapseapi.api.menu.action.MenuAction;
import ru.collapsedev.collapseapi.util.BukkitUtil;
import ru.collapsedev.collapseapi.util.PlayerUtil;
import ru.collapsedev.collapseapi.util.StringUtil;

import java.util.Map;

public class DefaultMenuActions {

    public static final Map<String, MenuAction> actions = Map.of(
            "[command]", ((player, clickType, quote) -> BukkitUtil.executeCommandPlaceholdered(player, quote)),
            "[sound]", (player, clickType, quote) -> PlayerUtil.playSound(player, quote),
            "[message]", (player, clickType, quote) -> PlayerUtil.sendMessage(player, quote),
            "[broadcast]", (player, clickType, quote) -> PlayerUtil.broadcast(StringUtil.applyPlaceholders(player, quote)),
            "[exit]", (player, clickType, quote) -> player.closeInventory(),
            "[player]", (player, clickType, quote) -> PlayerUtil.sendCommand(player, quote),
            "[title]", (player, clickType, quote) -> PlayerUtil.sendTitle(player, quote),
            "[actionbar]", (player, clickType, quote) -> PlayerUtil.sendActionBar(player, quote)
    );

    public static void apply(Menu menu) {
        actions.forEach(menu::addAction);
    }
}
