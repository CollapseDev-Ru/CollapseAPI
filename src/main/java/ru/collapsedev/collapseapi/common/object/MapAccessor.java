package ru.collapsedev.collapseapi.common.object;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import ru.collapsedev.collapseapi.builder.ItemBuilder;
import ru.collapsedev.collapseapi.util.ObjectUtil;
import ru.collapsedev.collapseapi.util.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class MapAccessor {
    Map<?, ?> map;
    @Nullable
    String name;

    private static final MapAccessor EMPTY_ACCESSOR = MapAccessor.of(new HashMap<>());

    public static MapAccessor getEmptyAccessor() {
        return EMPTY_ACCESSOR;
    }

    public static MapAccessor of(Map<?, ?> map, String name) {
        return new MapAccessor(map, name);
    }

    public static MapAccessor of(Map<?, ?> map) {
        return of(map, null);
    }

    public static MapAccessor of(ConfigurationSection section) {
        return of(section.getValues(true), section.getName());
    }

    public MapAccessor getAccessor(String key, MapAccessor defaultValue) {
        Map<?, ?> map = getMap(key);
        if (map.isEmpty()) {
            return defaultValue;
        }
        return MapAccessor.of(map, key);
    }

    public MapAccessor getAccessor(String key) {
        return getAccessor(key, getEmptyAccessor());
    }

    public List<MapAccessor> getListAccessors(String key) {
        List<Map<?, ?>> list = getOrDefault(key, Collections.emptyList());
        return ObjectUtil.mapList(list, MapAccessor::of);
    }

    public List<List<MapAccessor>> getListListAccessors(String key) {
        List<List<Map<?, ?>>> list = getOrDefault(key, Collections.emptyList());
        return ObjectUtil.mapList(list, val -> ObjectUtil.mapList(val, MapAccessor::of));
    }

    public List<MapAccessor> getListAccessors() {
        return new ArrayList<>(getMapAccessors().values());
    }

    public Map<String, MapAccessor> getMapAccessors() {
        Set<? extends Map.Entry<String, Map<?, ?>>> entries
                = ObjectUtil.castValue(map.entrySet());

        return entries.stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> MapAccessor.of(entry.getValue(), entry.getKey()))
        );
    }

    public String getString(String key, String defaultValue) {
        return getOrDefault(key, defaultValue);
    }

    public @Nullable String getString(String key) {
        return getString(key, null);
    }

    public String getColorizeString(String key) {
        return StringUtil.color(ObjectUtil.getOrDefault(getString(key), ""));
    }

    public Integer getInt(String key, Integer defaultValue) {
        Number number = getNumber(key, defaultValue);
        return number == null ? null : number.intValue();
    }

    public @Nullable Integer getInt(String key) {
        return getInt(key, null);
    }

    public Long getLong(String key, Long defaultValue) {
        Number number = getNumber(key, defaultValue);
        return number == null ? null : number.longValue();
    }

    public @Nullable Long getLong(String key) {
        return getLong(key, null);
    }

    public Float getFloat(String key, Float defaultValue) {
        Number number = getNumber(key, defaultValue);
        return number == null ? null : number.floatValue();
    }

    public @Nullable Float getFloat(String key) {
        return getFloat(key, null);
    }

    public Double getDouble(String key, Double defaultValue) {
        Number number = getNumber(key, defaultValue);
        return number == null ? null : number.doubleValue();
    }

    public @Nullable Double getDouble(String key) {
        return getDouble(key, null);
    }

    public Number getNumber(String key, Number defaultValue) {
        return getOrDefault(key, defaultValue);
    }

    public @Nullable Number getNumber(String key) {
        return getNumber(key, null);
    }

    public <T> List<T> getList(String key, List<T> defaultValue) {
        return getOrDefault(key, defaultValue);
    }

    public <T> List<T> getList(String key) {
        return getList(key, Collections.emptyList());
    }

    public List<String> getStringList(String key, List<String> defaultValue) {
        return getOrDefault(key, defaultValue);
    }

    public List<String> getStringList(String key) {
        return getStringList(key, Collections.emptyList());
    }

    public List<String> getColorizeStringList(String key) {
        return StringUtil.color(getStringList(key, Collections.emptyList()));
    }

    public List<Integer> getIntegerList(String key, List<Integer> defaultValue) {
        return getOrDefault(key, defaultValue);
    }

    public List<Integer> getIntegerList(String key) {
        return getIntegerList(key, Collections.emptyList());
    }

    public boolean getBoolean(String key, Boolean defaultValue) {
        return getOrDefault(key, defaultValue);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public Map<?, ?> getMap(String key, Map<?, ?> defaultValue) {
        return getOrDefault(key, defaultValue);
    }

    public Map<?, ?> getMap(String key) {
        return getMap(key, Collections.emptyMap());
    }

    public List<Map<?, ?>> getMapList(String key, List<Map<?, ?>> defaultValue) {
        return getOrDefault(key, defaultValue);
    }

    public List<Map<?, ?>> getMapList(String key) {
        return getMapList(key, Collections.emptyList());
    }

    public ItemStack getItemStack(String key, ItemStack defaultValue) {
        return ObjectUtil.getOrDefault(ItemStack.deserialize(getValue(key)), defaultValue);
    }

    public @Nullable ItemStack getItemStack(String key) {
        return getItemStack(key, null);
    }

    public ItemStack buildItem(String key, Placeholders placeholders) {
        return ItemBuilder.builder()
                .setAccessor(getAccessor(key))
                .setPlaceholders(placeholders)
                .buildFields().buildItem();
    }

    public ItemStack buildItem(String key) {
        return buildItem(key, Placeholders.EMPTY);
    }

    public Message getMessage(String key, Message defaultValue) {
        Object msg = map.get(key);
        if (msg == null) {
            return defaultValue;
        }
        return Message.of(msg);
    }

    public Message getMessage(String key) {
        return getMessage(key, Message.getEmptyMessage());
    }

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public <T> Optional<T> getOptional(String key) {
        return Optional.ofNullable(ObjectUtil.castValue(map.get(key)));
    }

    public <T> T getOrDefault(String key, T defaultValue) {
        return ObjectUtil.getOrDefault(getValue(key), defaultValue);
    }

    public <T> T getValue(String key) {
        return ObjectUtil.castValue(map.get(key));
    }

}