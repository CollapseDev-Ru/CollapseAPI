package ru.collapsedev.collapseapi.util;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
public class ObjectUtil {

    @SuppressWarnings("unchecked")
    public <V> V castValue(Object map) {
        return (V) map;
    }

    public <V> V getOrDefault(V value, V defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static <T, R> List<R> mapList(List<T> list, Function<T, R> mapper) {
        return list.stream().map(mapper).collect(Collectors.toList());
    }

    public static <T, R> Set<R> mapSet(Set<T> list, Function<T, R> mapper) {
        return list.stream().map(mapper).collect(Collectors.toSet());
    }

}
