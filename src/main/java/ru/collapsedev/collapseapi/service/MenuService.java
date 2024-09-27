package ru.collapsedev.collapseapi.service;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import ru.collapsedev.collapseapi.APILoader;
import ru.collapsedev.collapseapi.common.menu.MenuImpl;
import ru.collapsedev.collapseapi.api.menu.action.MenuAction;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import ru.collapsedev.collapseapi.common.object.Pair;
import ru.collapsedev.collapseapi.util.CooldownUtil;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class MenuService implements Listener {

    private static final long rattling = 250;
    private static final String cooldownType = "menuservice-click";

    public MenuService() {
        Bukkit.getPluginManager().registerEvents(this, APILoader.getInstance());
    }

    public MenuImpl getMenu(Inventory inv) {
        if (inv != null && inv.getHolder() instanceof MenuImpl) {
            return (MenuImpl) inv.getHolder();
        }
        return null;
    }

    @EventHandler()
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();

        MenuImpl menu = getMenu(event.getView().getTopInventory());
        if (menu == null) {
            return;
        }

        if (event.getClickedInventory() == null || event.getClickedInventory().getHolder() == null) {
            return;
        }

        InventoryHolder clickHolder = event.getClickedInventory().getHolder();
        InventoryHolder topHolder = event.getView().getTopInventory().getHolder();

        int slot = event.getSlot();

        if (!menu.getDraggableSlots().isEmpty()) {
            if (clickHolder.equals(topHolder)) {
                if (!menu.isDraggableSlot(slot)) {
                    event.setCancelled(true);
                }
            }
        } else {
            event.setCancelled(true);
            if (CooldownUtil.isCooldown(uuid, cooldownType)) {
                return;
            }
        }

        if (clickHolder.equals(topHolder)) {
            List<Pair<MenuAction, String>> actions = menu.actionSlots.getOrDefault(slot, Collections.emptyList());
            actions.forEach(pair -> pair.getFirst().onAction(player, event.getClick(), pair.getSecond()));

            CooldownUtil.setCooldown(uuid, cooldownType, rattling);
        }

    }
}

