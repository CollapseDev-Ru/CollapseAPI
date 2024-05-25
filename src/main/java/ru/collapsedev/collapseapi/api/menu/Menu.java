package ru.collapsedev.collapseapi.api.menu;

import ru.collapsedev.collapseapi.api.menu.item.CustomItem;
import ru.collapsedev.collapseapi.api.menu.action.MenuAction;
import org.bukkit.inventory.ItemStack;
import ru.collapsedev.collapseapi.common.object.Placeholders;

import java.util.List;
import java.util.Map;

public interface Menu {

    Menu build();
    Menu setPlaceholders(Placeholders placeholders);
    void addAction(String pattern, MenuAction action);
    void open();
    void setItems(ItemStack item, List<Integer> slots);
    void setItems(ItemStack item, int... slots);
    void setItems(ItemStack item, String type);
    void setCustomItem(CustomItem customItem);
    Map<String, List<Integer>> getTypeItems(String type);

}
