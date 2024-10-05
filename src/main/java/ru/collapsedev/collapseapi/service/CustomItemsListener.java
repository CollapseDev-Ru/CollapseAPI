package ru.collapsedev.collapseapi.service;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;
import ru.collapsedev.collapseapi.APILoader;
import ru.collapsedev.collapseapi.api.item.CustomItem;
import ru.collapsedev.collapseapi.common.item.ItemActionType;
import ru.collapsedev.collapseapi.common.item.ItemActionWrapper;

import java.util.List;

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
        PlayerInventory inventory = event.getPlayer().getInventory();
        CustomItemsService.executeItemAction(
                ItemActionType.ENTITY.wrap(event),
                event.getPlayer(),
                inventory.getItem(event.getHand())
        );
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent event) {
        CustomItemsService.executeItemAction(
                ItemActionType.EAT.wrap(event),
                event.getPlayer(),
                event.getItem()
        );
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        if (!event.isSneaking()) {
            return;
        }

        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();

        CustomItemsService.executeItemAction(
                ItemActionType.SHIFT.wrap(event),
                player,
                CustomItemsService.getSelectedItem(inventory)
        );
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        CustomItemsService.executeItemAction(
                ItemActionType.PLACE.wrap(event),
                player,
                event.getItemInHand()
        );
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        CustomItemsService.executeItemAction(
                ItemActionType.BREAK.wrap(event),
                player,
                CustomItemsService.getSelectedItem(player.getInventory())
        );
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        CustomItemsService.executeItemAction(
                ItemActionType.DROP.wrap(event),
                event.getPlayer(),
                event.getItemDrop().getItemStack()
        );
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        CustomItemsService.executeItemAction(
                ItemActionType.SWAP.wrap(event),
                event.getPlayer(),
                event.getOffHandItem()
        );
    }

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            CustomItemsService.executeItemAction(
                    ItemActionType.PICKUP.wrap(event),
                    player,
                    event.getItem().getItemStack()
            );
        }
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        CustomItemsService.unregisterItems(event.getPlugin());
    }
}
