package ru.collapsedev.collapseapi.common.menu;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import ru.collapsedev.collapseapi.api.menu.Menu;
import ru.collapsedev.collapseapi.api.menu.item.CustomItem;
import ru.collapsedev.collapseapi.builder.ItemBuilder;
import ru.collapsedev.collapseapi.api.menu.action.MenuAction;
import ru.collapsedev.collapseapi.common.object.Pair;
import ru.collapsedev.collapseapi.common.object.Placeholders;
import ru.collapsedev.collapseapi.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MenuImpl implements InventoryHolder, Cloneable, Menu {

    @Getter
    private final ConfigurationSection menuSection;
    @Getter
    private final Player target;

    private final Inventory inventory;
    private final List<String> inventoryWords;
    private final List<String> words;
    @Setter
    private List<Integer> draggableSlots = new ArrayList<>();

    public final Map<Integer, List<Pair<MenuAction, String>>> actionSlots = new HashMap<>();

    private Placeholders placeholders = Placeholders.EMPTY;

    public MenuImpl(ConfigurationSection menuSection, Player target) {
        this.menuSection = menuSection;
        this.target = target;

        String title = StringUtil.color(menuSection.getString("title"));

        List<String> layout = menuSection.getStringList("inventory");

        this.words = new ArrayList<>(menuSection
                .getConfigurationSection("words"
                ).getKeys(false));

        this.inventoryWords = getInventoryWords(layout);
        this.inventory = Bukkit.createInventory(this, inventoryWords.size(), title);
    }

    public MenuImpl(ConfigurationSection menuSection, List<CustomItem> items, String type, Player target) {
        this(menuSection, target);
        setCustomItems(items, type);
    }

    public void setCustomItems(List<CustomItem> items, String type) {
        List<CustomItem> itemList = new ArrayList<>(items);
        List<Integer> slots = new ArrayList<>();
        getTypeItems(type).values().forEach(slots::addAll);

        for (int slot : slots) {
            if (itemList.isEmpty()) {
                break;
            }

            itemList.get(0).setSlots(slot);

            setCustomItem(itemList.get(0));
            itemList.remove(0);
        }
    }

    public Menu build() {
        Map<String, List<Integer>> items = getItems();

        items.forEach((word, slots) -> {
            ConfigurationSection itemSection = menuSection
                    .getConfigurationSection("words." + word);

            ItemStack item = createItem(itemSection, placeholders, target);
            setItems(item, slots);
        });
        return this;
    }

    public static ItemStack createItem(ConfigurationSection itemSection,
                                       Placeholders placeholders, Player target) {
        return ItemBuilder.builder()
                .setSection(itemSection)
                .setPlaceholders(placeholders)
                .setTitleIsNullSetEmpty(true)
                .setUsePlaceholders(target)
                .buildFields().buildItem();
    }

    public Menu setPlaceholders(Placeholders placeholders) {
        this.placeholders = placeholders;
        return this;
    }

    private Map<Integer, List<String>> getActions(String actionPattern) {
        AtomicInteger slot = new AtomicInteger();

        Map<Integer, List<String>> actionsMap = new HashMap<>();
        this.inventoryWords.forEach(invWord -> {
            ConfigurationSection itemSection = menuSection
                    .getConfigurationSection("words." + invWord);

            if (itemSection == null || !itemSection.isList("actions")) {
                slot.getAndIncrement();
                return;
            }

            List<String> itemActions = itemSection.getStringList("actions");

            List<String> actions = itemActions.stream()
                    .filter(action -> action.startsWith(actionPattern))
                    .map(action -> {
                        String cleanAction = action.substring(actionPattern.length()).trim();
                        return cleanAction.isEmpty() ? null : cleanAction;
                    })
                    .collect(Collectors.toList());

            actionsMap.put(slot.getAndIncrement(), actions);
        });

        return actionsMap;
    }

    public void addAction(String pattern, MenuAction action) {
        Map<Integer, List<String>> actions = getActions(pattern);

        actions.forEach((key, value1) -> {
            List<Pair<MenuAction, String>> menuActions = actionSlots
                    .computeIfAbsent(key, k -> new ArrayList<>());

            for (String value : value1) {
                menuActions.add(Pair.of(action, value));
            }
            actionSlots.put(key, menuActions);
        });
    }

    public void open() {
        this.target.openInventory(this.inventory);
    }


    public void setItems(ItemStack item, List<Integer> slots) {
        slots.forEach(slot -> this.inventory.setItem(slot, item));
    }

    public void setItems(ItemStack item, int... slots) {
        Arrays.stream(slots).forEach(slot -> this.inventory.setItem(slot, item));
    }

    public void setItems(ItemStack item, String type) {
        Map<String, List<Integer>> items = getTypeItems(type);

        items.forEach((word, slots) -> setItems(item, slots));
    }

    public void addDraggableSlots(List<Integer> slots) {
        this.draggableSlots.addAll(slots);
    }

    public void addDraggableSlot(int slot) {
        this.draggableSlots.add(slot);
    }

    public boolean isDraggableSlot(int slot) {
        return this.draggableSlots.contains(slot);
    }

    public List<ItemStack> getDraggableItems() {
        List<ItemStack> items = new ArrayList<>();
        for (Integer slot : draggableSlots) {
            ItemStack item = inventory.getItem(slot);
            if (!(item == null || item.getType() == Material.AIR)) {
                items.add(item);
            }
        }
        return items;
    }

    public void setCustomItem(CustomItem customItem) {
        ItemStack item = customItem.getItem();
        List<Integer> slots = customItem.getSlots();
        MenuAction action = customItem.getAction();

        setItems(item, slots);

        slots.forEach(slot -> {
            List<Pair<MenuAction, String>> menuActions = actionSlots
                    .computeIfAbsent(slot, k -> new ArrayList<>());

            menuActions.add(Pair.of(action, null));
        });
    }

    public Map<String, List<Integer>> getTypeItems(String type) {
        Map<String, List<Integer>> slots = new HashMap<>();

        words.forEach(word -> {
            ConfigurationSection wordSection = menuSection
                    .getConfigurationSection("words." + word);

            if (!wordSection.contains("type") || !wordSection
                    .getString("type").equals(type)) {
                return;
            }

            List<Integer> wordSlots = new ArrayList<>();

            AtomicInteger index = new AtomicInteger();
            inventoryWords.forEach(invWord -> {
                if (word.equals(invWord)) {
                    wordSlots.add(index.get());
                }
                index.getAndIncrement();
            });

            slots.put(word, wordSlots);
        });

        return slots;
    }

    private Map<String, List<Integer>> getItems() {
        Map<String, List<Integer>> slots = new HashMap<>();

        words.forEach(word -> {
            if (menuSection.getConfigurationSection("words")
                    .getConfigurationSection(word).contains("type")) {
                return;
            }

            List<Integer> wordSlots = new ArrayList<>();

            AtomicInteger index = new AtomicInteger();
            inventoryWords.forEach(invWord -> {
                if (word.equals(invWord)) {
                    wordSlots.add(index.get());
                }
                index.getAndIncrement();
            });

            slots.put(word, wordSlots);
        });

        return slots;
    }

    private List<String> getInventoryWords(List<String> lines) {
        return lines.stream()
                .flatMap(line -> Arrays.stream(line.split("")))
                .collect(Collectors.toList());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }


    @Override
    public MenuImpl clone() {
        MenuImpl clonedMenu = new MenuImpl(this.menuSection, this.target);
        clonedMenu.actionSlots.putAll(this.actionSlots);
        clonedMenu.placeholders = this.placeholders;
        clonedMenu.build();

        return clonedMenu;
    }
}