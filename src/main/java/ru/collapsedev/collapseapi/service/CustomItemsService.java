package ru.collapsedev.collapseapi.service;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import io.github.bananapuncher714.nbteditor.NBTEditor;

import java.util.*;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import ru.collapsedev.collapseapi.api.item.CustomItem;
import ru.collapsedev.collapseapi.common.item.ItemActionWrapper;

@UtilityClass
public class CustomItemsService {
    @Getter
    private final String customItemKey = "CustomItem";

    private final Table<String, Plugin, CustomItem> items = HashBasedTable.create();

    public void setItem(Plugin plugin, String itemName, CustomItem customItem) {
        items.put(itemName, plugin, customItem);
    }

    public void removeItem(Plugin plugin, String itemName) {
        items.remove(itemName, plugin);
    }

    public void unregisterItems(Plugin plugin) {
        items.cellSet().removeIf(cell -> cell.getColumnKey() == plugin);
    }

    public String getItemName(ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
        return NBTEditor.getString(itemStack, customItemKey);
    }

    public boolean hasCustomItem(ItemStack itemStack) {
        return getItemName(itemStack) != null;
    }

    public boolean hasCustomItem(Plugin plugin, ItemStack itemStack) {
        String itemName = getItemName(itemStack);
        if (itemName == null) {
            return false;
        }

        return items.contains(itemName, plugin);
    }

    public Map<Plugin, CustomItem> getPluginItemsByName(String itemName) {
        return items.row(itemName);
    }

    public Map<String, CustomItem> getPluginItems(Plugin plugin) {
        return items.column(plugin);
    }

    public List<CustomItem> getCustomItemsByName(String itemName) {
        return new ArrayList<>(items.row(itemName).values());
    }

    public Map<Plugin, CustomItem> getItemsByItem(ItemStack itemStack) {
        String itemName = getItemName(itemStack);
        if (itemName == null) {
            return Collections.emptyMap();
        }
        return getPluginItemsByName(itemName);
    }
    public List<CustomItem> getCustomItemsByItem(ItemStack itemStack) {
        String itemName = getItemName(itemStack);
        if (itemName == null) {
            return Collections.emptyList();
        }
        return getCustomItemsByName(itemName);
    }

    public CustomItem getPluginItemByName(Plugin plugin, String itemName) {
        return items.get(itemName, plugin);
    }

    public CustomItem getPluginItemByItem(Plugin plugin, ItemStack itemStack) {
        String itemName = NBTEditor.getString(itemStack, customItemKey);
        return items.get(itemName, plugin);
    }

    public List<CustomItem> getAllPluginItems(Plugin plugin) {
        return new ArrayList<>(items.column(plugin).values());
    }

    public static void executeItemAction(ItemActionWrapper<?> wrapper, Player player, ItemStack item) {
        if (item == null) {
            return;
        }


        List<CustomItem> customItems = getCustomItemsByItem(item);
        customItems.removeIf(customItem -> !(customItem.isClickable()
                && customItem.getActionTypes().contains(wrapper.getActionType())));

        customItems.forEach(customItem -> customItem.getAction().useAction(wrapper, player, item));
    }

    public static ItemStack getSelectedItem(PlayerInventory inventory) {
        ItemStack mainHandItem = inventory.getItemInMainHand();
        ItemStack offHandItem = inventory.getItemInOffHand();

        return hasCustomItem(mainHandItem) ? mainHandItem : offHandItem;
    }


}
