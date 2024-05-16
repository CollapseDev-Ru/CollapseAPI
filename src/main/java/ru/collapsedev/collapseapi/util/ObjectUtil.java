package ru.collapsedev.collapseapi.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ObjectUtil {

    @SuppressWarnings("unchecked")
    public <V> V castValue(Object map) {
        return (V) map;
    }
}
