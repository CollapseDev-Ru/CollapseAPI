package ru.collapsedev.collapseapi.common.object;

import com.cryptomorin.xseries.XSound;
import org.bukkit.Bukkit;
import ru.collapsedev.collapseapi.api.menu.Menu;
import ru.collapsedev.collapseapi.api.menu.action.MenuAction;
import ru.collapsedev.collapseapi.util.StringUtil;

import java.util.Map;

public class DefaultMenuActions {

    public static final Map<String, MenuAction> actions = Map.of(
            "[command]", ((player, clickType, quote) -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    StringUtil.applyPlaceholders(player, quote))),
            "[sound]", (player, clickType, quote) -> XSound.parse(quote).soundPlayer().forPlayers(player).play(),
            "[message]", (player, clickType, quote) -> player.sendMessage(StringUtil.placeholdersColor(player, quote)),
            "[broadcast]", (player, clickType, quote) -> {
                String msg = StringUtil.color(quote);
                Bukkit.getOnlinePlayers().forEach(all -> all.sendMessage(msg));
            },
            "[exit]", (player, clickType, quote) -> player.closeInventory()
    );

    public static void apply(Menu menu) {
        actions.forEach(menu::addAction);
    }
}
