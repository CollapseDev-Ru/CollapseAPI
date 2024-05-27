package ru.collapsedev.collapseapi.common.menu;

import ru.collapsedev.collapseapi.api.menu.Menu;
import ru.collapsedev.collapseapi.api.menu.PagedMenu;
import ru.collapsedev.collapseapi.api.menu.item.CustomItem;
import ru.collapsedev.collapseapi.api.menu.action.MenuAction;
import ru.collapsedev.collapseapi.builder.ItemBuilder;
import ru.collapsedev.collapseapi.common.menu.item.CustomItemImpl;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PagedMenuImpl implements PagedMenu {

    private final Menu menu;

    private final List<Menu> pages = new ArrayList<>();
    private int page = 0;
    private int pageSize;
    private ItemStack backItem = new ItemStack(Material.BARRIER);
    private ItemStack nextItem = new ItemStack(Material.BARRIER);

    public PagedMenuImpl(Menu menu, List<CustomItem> items, String type) {
        this.menu = menu;

        List<Integer> slots = new ArrayList<>();
        menu.getTypeItems(type).values().forEach(slots::addAll);

        int pageSize = (int) Math.max(1, Math.ceil((double) items.size() / slots.size()));

        init(menu, pageSize);
        setCustomItems(items, type);

        setBackItem(createPagedItem("back"));
        setNextItem(createPagedItem("next"));
    }

    private void init(Menu menu, int pageSize) {
        this.pageSize = pageSize;

        for (int i = 0; i < pageSize; i++) {
            pages.add(((MenuImpl) menu).clone());
        }
    }

    public PagedMenu build() {
        for (int i = 0; i < this.pageSize; i++) {
            if (i < this.pageSize - 1) {
                setNextItemToMenu(this.pages.get(i));
            }
            if (i > 0) {
                setBackItemToMenu(this.pages.get(i));
            }
        }
        return this;
    }

    private void setBackItemToMenu(Menu menu) {
        setPagedItemToMenu(menu, "back", backItem, (player, clickType, quote) -> openBackPage());
    }

    private void setNextItemToMenu(Menu menu) {
        setPagedItemToMenu(menu, "next", nextItem, (player, clickType, quote) -> openNextPage());
    }

    private void setPagedItemToMenu(Menu menu, String type, ItemStack item, MenuAction action) {
        menu.getTypeItems(type)
                .values()
                .forEach(list
                        -> list.forEach(slot
                        -> menu.setCustomItem(new CustomItemImpl(item, action, slot)))
                );
    }

    private void openNextPage() {
        this.page++;

        if (this.page > pages.size() - 1) {
            return;
        }

        pages.get(this.page).open();
    }

    private void openBackPage() {
        this.page--;

        if (this.page < 0) {
            return;
        }

        pages.get(this.page).open();
    }

    public PagedMenu setBackItem(ItemStack item) {
        this.backItem = item;
        return this;
    }

    public PagedMenu setNextItem(ItemStack item) {
        this.nextItem = item;
        return this;
    }

    private void setCustomItems(List<CustomItem> items, String type) {
        this.setCustomItems(0, items, type);
    }

    public void setCustomItems(int page, List<CustomItem> items, String type) {
        if (this.pages.size() == page) {
            return;
        }

        Menu menu = this.pages.get(page);

        List<CustomItem> itemList = new ArrayList<>(items);
        List<Integer> slots = new ArrayList<>();
        menu.getTypeItems(type).values().forEach(slots::addAll);

        for (int slot : slots) {
            if (itemList.isEmpty()) {
                return;
            }

            itemList.get(0).setSlots(slot);

            menu.setCustomItem(itemList.get(0));
            itemList.remove(0);
        }

        if (!itemList.isEmpty()) {
            setCustomItems(page + 1, itemList, type);
        }
    }

    private ItemStack createPagedItem(String type) {
        return ItemBuilder.builder()
                .setSection(this.menu.getMenuSection().getConfigurationSection(type))
                .setUsePlaceholders(this.menu.getTarget())
                .buildFields().buildItem();
    }

    public void open(int page) {
        this.pages.get(page).open();
    }

    public void open() {
        open(0);
    }

}
