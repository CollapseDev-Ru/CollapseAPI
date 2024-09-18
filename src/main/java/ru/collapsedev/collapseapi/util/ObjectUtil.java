package ru.collapsedev.collapseapi.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ObjectUtil {

    @SuppressWarnings("unchecked")
    public <V> V castValue(Object map) {
        return (V) map;
    }

    public <V> V getOrDefault(V value, V defaultValue) {
        return value != null ? value : defaultValue;
    }
}
