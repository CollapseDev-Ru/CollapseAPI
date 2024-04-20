package ru.collapsedev.collapseapi.api.menu.item;

import ru.collapsedev.collapseapi.api.menu.action.MenuAction;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface CustomItem {
    ItemStack getItem();

    MenuAction getAction();

    List<Integer> getSlots();

    void setSlots(int... slots);

    void setSlots(List<Integer> slots);
}
