package ru.collapsedev.collapseapi.api.action;


import org.bukkit.entity.Player;
import ru.collapsedev.collapseapi.api.menu.action.MenuAction;

@FunctionalInterface
public interface Action {

    void onAction(Player player, String quote);

    default MenuAction toMenuAction() {
        return (player, clickType, quote) -> onAction(player, quote);
    }
}
