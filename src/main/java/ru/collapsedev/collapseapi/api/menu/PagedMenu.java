package ru.collapsedev.collapseapi.api.menu;

import ru.collapsedev.collapseapi.api.menu.item.CustomItem;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface PagedMenu {

    PagedMenu setBackItem(ItemStack item);
    PagedMenu setNextItem(ItemStack item);
    void setCustomItems(int page, List<CustomItem> items, String type);
    void open();
    void open(int page);
    PagedMenu build();
    int getCurrentPage();
}
