package ru.collapsedev.collapseapi.api.menu.action;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

@FunctionalInterface
public interface MenuAction {

    void onAction(Player player, ClickType clickType, String quote);

}