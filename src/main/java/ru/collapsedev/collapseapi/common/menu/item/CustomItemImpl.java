package ru.collapsedev.collapseapi.common.menu.item;

import lombok.Getter;
import ru.collapsedev.collapseapi.api.menu.item.CustomItem;
import ru.collapsedev.collapseapi.api.menu.action.MenuAction;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class CustomItemImpl implements CustomItem {
    private ItemStack item;
    private MenuAction action;
    private final List<Integer> slots = new ArrayList<>();

    public CustomItemImpl(ItemStack item) {
        init(item, (player, clickType, quote) -> {});
    }
    public CustomItemImpl(ItemStack item, MenuAction action, int... slots) {
        init(item, action);
        Arrays.stream(slots).forEach(this.slots::add);
    }

    public CustomItemImpl(ItemStack item, MenuAction action, List<Integer> slots) {
        init(item, action);
        this.slots.addAll(slots);
    }

    public CustomItemImpl(ItemStack item, MenuAction action) {
        init(item, action);
    }

    private void init(ItemStack item, MenuAction action) {
        this.item = item;
        this.action = action;
    }

    public void setSlots(int... slots) {
        Arrays.stream(slots).forEach(this.slots::add);
    }

    public void setSlots(List<Integer> slots) {
        this.slots.addAll(slots);
    }

}
