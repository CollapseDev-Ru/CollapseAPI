package ru.collapsedev.collapseapi.api.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.collapsedev.collapseapi.common.item.ItemActionType;
import ru.collapsedev.collapseapi.common.object.MapAccessor;

import java.util.Set;

public interface CustomItem extends ClickableItem {

    Plugin getPlugin();
    String getItemName();
    ItemStack getItem();
    ItemAction getAction();
    Set<ItemActionType> getActionTypes();

    MapAccessor getAccessor();
    String getTag(String key);

    void setAmount(int amount);
    void give(Player player);
    void give(Player player, int count);
    void setTag(String key, String tag);

    void register();
    void unregister();

    boolean isClickable();
    boolean hasTag(String key);

    void onAction(Set<ItemActionType> actionTypes, ItemAction action);

}
