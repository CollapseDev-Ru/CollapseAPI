package ru.collapsedev.collapseapi.api.menu;

import ru.collapsedev.collapseapi.api.menu.item.CustomItem;
import ru.collapsedev.collapseapi.api.menu.action.MenuAction;
import ru.collapsedev.collapseapi.api.menu.action.MenuQuoteAction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public interface Menu {

    Menu build();
    Menu addPlaceholder(String key, String value);
    Menu addPlaceholder(String key, List<String> value);
    Menu setPlaceholders(Map<String, List<String>> placeholders);
    void addAction(String actionName, MenuAction action);
    void addQuoteAction(String actionName, MenuQuoteAction action);
    void open();
    void setItems(ItemStack item, List<Integer> slots);
    void setItems(ItemStack item, int... slots);
    void setItems(ItemStack item, String type);
    void setCustomItem(CustomItem customItem);
    Map<String, List<Integer>> getTypeItems(String type);

}
