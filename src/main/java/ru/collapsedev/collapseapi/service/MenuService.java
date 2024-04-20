package ru.collapsedev.collapseapi.service;

import ru.collapsedev.collapseapi.APILoader;
import ru.collapsedev.collapseapi.common.menu.MenuImpl;
import ru.collapsedev.collapseapi.common.menu.action.AbstractMenuQuoteAction;
import ru.collapsedev.collapseapi.api.menu.action.IMenuAction;
import ru.collapsedev.collapseapi.api.menu.action.MenuAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class MenuService implements Listener {

    public MenuService() {
        Bukkit.getPluginManager().registerEvents(this, APILoader.getInstance());
    }


    public MenuImpl getMenu(InventoryView view) {
        Inventory inv = view.getTopInventory();
        return !(inv.getHolder() instanceof MenuImpl) ? null : (MenuImpl) inv.getHolder();
    }

    @EventHandler()
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        MenuImpl menu = getMenu(event.getView());

        if (menu == null) {
            return;
        }

        event.setCancelled(true);

        List<IMenuAction> actions = menu.actionSlots.get(event.getSlot());

        if (actions == null) {
            return;
        }

        actions.forEach(action -> {
            if (action != null) {
                if (action instanceof AbstractMenuQuoteAction) {
                    AbstractMenuQuoteAction quoteAction = (AbstractMenuQuoteAction) action;
                    quoteAction.onAction(player, event.getClick(), quoteAction.getQuote());
                } else {
                    ((MenuAction) action).onAction(player, event.getClick());
                }
            }
        });

    }
}
