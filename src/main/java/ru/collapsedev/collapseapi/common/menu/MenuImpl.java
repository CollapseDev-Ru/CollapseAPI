package ru.collapsedev.collapseapi.common.menu;

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

    private final ConfigurationSection section;
    private final Player target;

    private Inventory inventory;
    private List<String> inventoryWords;
    private List<String> words;
    public final Map<Integer, List<Pair<MenuAction, String>>> actionSlots = new HashMap<>();
    private Placeholders placeholders = Placeholders.EMPTY;

    public MenuImpl(ConfigurationSection section, Player target) {
        this.section = section;
        this.target = target;

        String title = StringUtil.color(section.getString("title"));

        List<String> layout = section.getStringList("inventory");

        this.words = new ArrayList<>(section.getConfigurationSection("words").getKeys(false));

        this.inventoryWords = getInventoryWords(layout);
        this.inventory = Bukkit.createInventory(this, inventoryWords.size(), title);
    }

    public Menu build() {
        Map<String, List<Integer>> items = getItems();

        items.forEach((word, slots) -> {
            ConfigurationSection itemSection = section.getConfigurationSection("words." + word);
            ItemStack itemStack = ItemBuilder.builder()
                    .setSection(itemSection)
                    .setPlaceholders(placeholders)
                    .setTitleIsNullSetEmpty(true)
                    .setUsePlaceholders(target)
                    .buildFields().buildItem();

            setItems(itemStack, slots);
        });
        return this;
    }

    public Menu setPlaceholders(Placeholders placeholders) {
        this.placeholders = placeholders;
        return this;
    }

    private Map<Integer, List<String>> getActions(String actionPattern) {
        AtomicInteger slot = new AtomicInteger();

        Map<Integer, List<String>> actionsMap = new HashMap<>();
        this.inventoryWords.forEach(invWord -> {
            ConfigurationSection itemSection = section.getConfigurationSection("words." + invWord);

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
            List<Pair<MenuAction, String>> menuActions = actionSlots.computeIfAbsent(key, k -> new ArrayList<>());
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

    public void setCustomItem(CustomItem customItem) {
        ItemStack item = customItem.getItem();
        List<Integer> slots = customItem.getSlots();
        MenuAction action = customItem.getAction();

        setItems(item, slots);

        slots.forEach(slot -> {
            List<Pair<MenuAction, String>> menuActions = actionSlots.computeIfAbsent(slot, k -> new ArrayList<>());
            menuActions.add(Pair.of(action, null));
        });
    }

    public Map<String, List<Integer>> getTypeItems(String type) {
        Map<String, List<Integer>> slots = new HashMap<>();

        words.forEach(word -> {
            ConfigurationSection wordSection = section.getConfigurationSection("words." + word);

            if (!wordSection.contains("type") || !wordSection.getString("type").equals(type)) {
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
        MenuImpl clonedMenu = new MenuImpl(this.section, this.target);
        clonedMenu.actionSlots.putAll(this.actionSlots);
        clonedMenu.placeholders = this.placeholders;
        clonedMenu.build();

        return clonedMenu;
    }
}