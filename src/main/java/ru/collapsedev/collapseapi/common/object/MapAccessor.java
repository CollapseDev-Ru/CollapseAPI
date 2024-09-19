package ru.collapsedev.collapseapi.common.object;

import lombok.Getter;
import ru.collapsedev.collapseapi.util.ObjectUtil;

import java.util.List;
import java.util.Map;

@Getter
public class MapAccessor {
    Map<?, ?> map;

    private MapAccessor(Map<?, ?> map) {
        this.map = map;
    }

    public String getString(String key) {
        return ObjectUtil.castValue(map.get(key));
    }

    public int getInt(String key) {
        return ((Number) map.get(key)).intValue();
    }

    public double getDouble(String key) {
        return ((Number) map.get(key)).doubleValue();
    }

    public List<?> getList(String key) {
        return ObjectUtil.castValue(map.get(key));
    }

    public List<String> getStringList(String key) {
        return ObjectUtil.castValue(map.get(key));
    }

    public boolean getBoolean(String key) {
        return ObjectUtil.castValue(map.get(key));
    }

    public static MapAccessor of(Map<?, ?> map) {
        return new MapAccessor(map);
    }
}