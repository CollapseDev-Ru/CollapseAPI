package ru.collapsedev.collapseapi.common.item;

import com.google.common.collect.Sets;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.collapsedev.collapseapi.api.item.CustomItem;
import ru.collapsedev.collapseapi.api.item.ItemAction;
import ru.collapsedev.collapseapi.builder.ItemBuilder;
import ru.collapsedev.collapseapi.common.object.MapAccessor;
import ru.collapsedev.collapseapi.service.CustomItemsService;

import java.util.Collections;
import java.util.Set;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomItemImpl implements CustomItem {

    final Plugin plugin;
    final String itemName;

    MapAccessor accessor;
    ItemStack item;

    boolean clickable;
    Set<ItemActionType> actionTypes = Collections.emptySet();
    ItemAction action;

    public CustomItemImpl(Plugin plugin, String itemName, ItemStack item) {
        this.plugin = plugin;
        this.itemName = itemName;
        this.item = item;

        addCustomTag();
    }

    public CustomItemImpl(Plugin plugin, String itemName, MapAccessor accessor) {
        this(plugin, itemName, ItemBuilder.builder()
                .setAccessor(accessor)
                .buildFields().buildItem());

        this.accessor = accessor;
    }

    public CustomItemImpl(Plugin plugin, String itemName, ConfigurationSection itemSection) {
        this(plugin, itemName, MapAccessor.of(itemSection.getValues(false)));
    }

    public CustomItemImpl(Plugin plugin, ConfigurationSection itemSection) {
        this(plugin, itemSection.getName(), itemSection);
    }


    private void addCustomTag() {
        this.item = NBTEditor.set(item, itemName, CustomItemsService.getCustomItemKey());
    }

    @Override
    public void register() {
        CustomItemsService.setItem(plugin, itemName, this);
    }

    @Override
    public void unregister() {
        CustomItemsService.removeItem(plugin, itemName);
    }

    @Override
    public void setAmount(int amount) {
        item.setAmount(Math.min(amount, item.getMaxStackSize()));
    }

    @Override
    public void give(Player player) {
        give(player, -1);
    }

    @Override
    public void give(Player player, int amount) {
        ItemStack cloneItem = item.clone();
        if (amount != -1) {
            cloneItem.setAmount(amount);
        }

        Location location = player.getLocation();
        World world = player.getWorld();

        player.getInventory().addItem(cloneItem)
                .forEach((slot, item) -> world.dropItem(location, item));
    }


    @Override
    public void setTag(String key, String tag) {
        item = NBTEditor.set(item, tag, key);
    }

    @Override
    public boolean hasTag(String key) {
        return NBTEditor.contains(item, key);
    }

    @Override
    public String getTag(String key) {
        return NBTEditor.getString(item, key);
    }


    @Override
    public void onAction(Set<ItemActionType> actionTypes, ItemAction action) {
        this.actionTypes = actionTypes;
        this.action = action;
        this.clickable = true;
    }

}
