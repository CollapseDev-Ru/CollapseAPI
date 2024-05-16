package ru.collapsedev.collapseapi.common.menu;

import ru.collapsedev.collapseapi.api.menu.Menu;
import ru.collapsedev.collapseapi.api.menu.item.CustomItem;
import ru.collapsedev.collapseapi.builder.ItemBuilder;
import ru.collapsedev.collapseapi.common.menu.action.AbstractMenuQuoteAction;
import ru.collapsedev.collapseapi.api.menu.action.IMenuAction;
import ru.collapsedev.collapseapi.api.menu.action.MenuAction;
import ru.collapsedev.collapseapi.api.menu.action.MenuQuoteAction;
import ru.collapsedev.collapseapi.util.ObjectUtil;
import ru.collapsedev.collapseapi.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MenuImpl implements InventoryHolder, Cloneable, Menu {


    private ConfigurationSection section;
    private Inventory inventory;
    private List<String> inventoryWords;
    private List<String> words;
    public final Map<Integer, List<IMenuAction>> actionSlots = new HashMap<>();
    private Map<String, List<String>> placeholders = new HashMap<>();

    public MenuImpl(ConfigurationSection section) {
        this.section = section;

        String title = StringUtil.color(section.getString("title"));

        List<String> layout = section.getStringList("inventory");

        this.words = new ArrayList<>(section.getConfigurationSection("words").getKeys(false));

        this.inventoryWords = getInventoryWords(layout);
        this.inventory = Bukkit.createInventory(this, inventoryWords.size(), title);
    }


//    public Menu cloneMenu() {
//        Menu clonedMenu = new Menu(this.menuSection);
//        clonedMenu.actionSlots.putAll(this.actionSlots);
//        clonedMenu.placeholders.putAll(this.placeholders);
//
//        if (this.isBuilded) {
//            clonedMenu.build();
//        }
//
//        return clonedMenu;
//    }

    public Menu build() {
        Map<String, List<Integer>> items = getItems();

        items.forEach((word, slots) -> {
            ConfigurationSection itemSection = section.getConfigurationSection("words." + word);
            ItemStack itemStack = ItemBuilder.builder()
                    .setSection(itemSection)
                    .setPlaceholders(placeholders)
                    .buildFields().buildItem();

            setItems(itemStack, slots);
        });
        return this;
    }

    public Menu addPlaceholder(String key, List<String> value) {
        this.placeholders.put(key, value);
        return this;
    }
    public Menu addPlaceholder(String key, String value) {
        this.placeholders.put(key, Collections.singletonList(value));
        return this;
    }

    public Menu setPlaceholders(Map<String, List<String>> placeholders) {
        this.placeholders = placeholders;
        return this;
    }

    enum ActionFilter {
        EQUALS, START_WITH;

        private boolean compare(String string1, String string2) {
            switch (this) {
                case EQUALS:
                    return string1.equals(string2);
                case START_WITH:
                    return string1.startsWith(string2);
                default:
                    return false;
            }
        }
    }

    private Map<Integer, List<String>> getActions(String actionName, ActionFilter filter) {
        AtomicInteger index = new AtomicInteger();

        Map<Integer, List<String>> actionsMap = new HashMap<>();
        this.inventoryWords.forEach(invWord -> {
            ConfigurationSection wordSection = section.getConfigurationSection("words." + invWord);

            if (wordSection == null || !wordSection.isList("actions")) {
                index.getAndIncrement();
                return;
            }

            List<String> actions = ObjectUtil.castValue(wordSection.get("actions"));
            actions = actions.stream()
                    .filter(wordAction -> filter.compare(wordAction, actionName))
                    .collect(Collectors.toList());

            actionsMap.put(index.getAndIncrement(), actions);
        });

        return actionsMap;
    }

    public void addAction(String actionName, MenuAction action) {
        Map<Integer, List<String>> actions = getActions(actionName, ActionFilter.EQUALS);
        actions.keySet().forEach(key -> {
            List<IMenuAction> menuActions = actionSlots.computeIfAbsent(key, k -> new ArrayList<>());
            menuActions.add(action);
            actionSlots.put(key, menuActions);
        });
    }

    public void addQuoteAction(String actionName, MenuQuoteAction action) {
        Map<Integer, List<String>> actions = getActions(actionName, ActionFilter.START_WITH);
        actions.forEach((key, values) -> {
            List<IMenuAction> menuActions = actionSlots.computeIfAbsent(key, k -> new ArrayList<>());
            values.forEach(quote -> {
                AbstractMenuQuoteAction abstractMenuQuoteAction = new AbstractMenuQuoteAction() {
                    @Override
                    public void onAction(Player player, ClickType clickType, String quote) {
                        action.onAction(player, clickType, quote);
                    }
                };
                abstractMenuQuoteAction.setQuote(StringUtil.splitQuote(actionName, quote));
                menuActions.add(abstractMenuQuoteAction);
            });
            actionSlots.put(key, menuActions);
        });
    }

    public void open(Player target) {
        target.openInventory(this.inventory);
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
            List<IMenuAction> menuActions = actionSlots.computeIfAbsent(slot, k -> new ArrayList<>());
            menuActions.add(action);
            actionSlots.put(slot, menuActions);
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
        MenuImpl clonedMenu = new MenuImpl(this.section);
        clonedMenu.actionSlots.putAll(this.actionSlots);
        clonedMenu.placeholders.putAll(this.placeholders);
        clonedMenu.build();

        return clonedMenu;
    }
}