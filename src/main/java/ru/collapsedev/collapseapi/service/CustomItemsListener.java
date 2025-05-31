package ru.collapsedev.collapseapi.service;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.PlayerInventory;
import ru.collapsedev.collapseapi.APILoader;
import ru.collapsedev.collapseapi.common.item.ItemActionType;

public class CustomItemsListener implements Listener {

    public CustomItemsListener() {
        Bukkit.getPluginManager().registerEvents(this, APILoader.getInstance());
    }

    private static boolean isRightClick(Action action) {
        return action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (isRightClick(event.getAction())) {
            CustomItemsService.executeItemAction(
                    ItemActionType.CLICK.wrap(event),
                    event.getPlayer(),
                    event.getItem()
            );
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.isCancelled()) return;

        PlayerInventory inventory = event.getPlayer().getInventory();
        event.setCancelled(CustomItemsService.executeItemAction(
                ItemActionType.ENTITY.wrap(event),
                event.getPlayer(),
                inventory.getItem(event.getHand())
        ));
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent event) {
        if (event.isCancelled()) return;

        event.setCancelled(CustomItemsService.executeItemAction(
                ItemActionType.EAT.wrap(event),
                event.getPlayer(),
                event.getItem()
        ));
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        if (event.isCancelled()) return;
        if (!event.isSneaking()) {
            return;
        }

        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();

        event.setCancelled(CustomItemsService.executeItemAction(
                ItemActionType.SHIFT.wrap(event),
                player,
                CustomItemsService.getSelectedItem(inventory)
        ));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        event.setCancelled(CustomItemsService.executeItemAction(
                ItemActionType.PLACE.wrap(event),
                player,
                event.getItemInHand()
        ));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        event.setCancelled(CustomItemsService.executeItemAction(
                ItemActionType.BREAK.wrap(event),
                player,
                CustomItemsService.getSelectedItem(player.getInventory())
        ));
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (event.isCancelled()) return;
        event.setCancelled(CustomItemsService.executeItemAction(
                ItemActionType.DROP.wrap(event),
                event.getPlayer(),
                event.getItemDrop().getItemStack()
        ));
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        if (event.isCancelled()) return;
        event.setCancelled(CustomItemsService.executeItemAction(
                ItemActionType.SWAP.wrap(event),
                event.getPlayer(),
                event.getOffHandItem()
        ));
    }

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent event) {
        if (event.isCancelled()) return;
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            event.setCancelled(CustomItemsService.executeItemAction(
                    ItemActionType.PICKUP.wrap(event),
                    player,
                    event.getItem().getItemStack()
            ));
        }
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        CustomItemsService.unregisterItems(event.getPlugin());
    }
}
