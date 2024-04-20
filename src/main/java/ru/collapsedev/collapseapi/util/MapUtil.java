package ru.collapsedev.collapseapi.util;

import lombok.experimental.UtilityClass;
import org.bukkit.configuration.MemorySection;

import java.util.Map;

@UtilityClass
public class MapUtil {

    @SuppressWarnings("unchecked")
    public Map<String, Object> getMapValues1(Map<String, Object> map, String key) {
        return (Map<String, Object>) map.get(key);
    }
    @SuppressWarnings("unchecked")
    public <V> V castValue(Object map) {
        return (V) map;
    }
}
