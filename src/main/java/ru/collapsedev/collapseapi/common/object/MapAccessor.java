package ru.collapsedev.collapseapi.common.object;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.inventory.ItemStack;
import ru.collapsedev.collapseapi.util.ObjectUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MapAccessor {
    Map<?, ?> map;

    private MapAccessor(Map<?, ?> map) {
        this.map = map;
    }

    public static MapAccessor of(Map<?, ?> map) {
        return new MapAccessor(map);
    }


    public String getString(String key) {
        if (!containsKey(key)) {
            return null;
        }
        return ObjectUtil.castValue(map.get(key));
    }

    public int getInt(String key) {
        if (!containsKey(key)) {
            return -1;
        }
        return ((Number) map.get(key)).intValue();
    }

    public long getLong(String key) {
        if (!containsKey(key)) {
            return -1;
        }
        return ((Number) map.get(key)).longValue();
    }

    public float getFloat(String key) {
        if (!containsKey(key)) {
            return -1;
        }
        return ((Number) map.get(key)).floatValue();
    }

    public double getDouble(String key) {
        if (!containsKey(key)) {
            return -1;
        }
        return ((Number) map.get(key)).doubleValue();
    }

    public List<?> getList(String key) {
        if (!containsKey(key)) {
            return Collections.emptyList();
        }
        return ObjectUtil.castValue(map.get(key));
    }

    public List<String> getStringList(String key) {
        if (!containsKey(key)) {
            return Collections.emptyList();
        }
        return ObjectUtil.castValue(map.get(key));
    }

    public boolean getBoolean(String key) {
        if (!containsKey(key)) {
            return false;
        }
        return ObjectUtil.castValue(map.get(key));
    }

    public Map<?, ?> getMap(String key) {
        if (!containsKey(key)) {
            return null;
        }
        return ObjectUtil.castValue(map.get(key));
    }

    public List<Map<?, ?>> getMapList(String key) {
        if (!containsKey(key)) {
            return Collections.emptyList();
        }
        return ObjectUtil.castValue(map.get(key));
    }

    public ItemStack getItemStack(String key) {
        if (!containsKey(key)) {
            return null;
        }
        return ObjectUtil.castValue(map.get(key));
    }

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public <T> T getOrDefault(String key, T defaultValue) {
        return ObjectUtil.getOrDefault(ObjectUtil.castValue(map.get(key)), defaultValue);
    }

    public <T> Optional<T> getOptional(String key) {
        return Optional.ofNullable(ObjectUtil.castValue(map.get(key)));
    }

}