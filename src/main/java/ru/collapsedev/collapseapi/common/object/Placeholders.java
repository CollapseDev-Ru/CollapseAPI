package ru.collapsedev.collapseapi.common.object;

import lombok.Getter;
import ru.collapsedev.collapseapi.util.StringUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class Placeholders {

    public static final Placeholders EMPTY = new Placeholders(Map.of());

    private final Map<String, List<String>> placeholders;

    private Placeholders(Map<String, List<String>> placeholders) {
        this.placeholders = placeholders;
    }

    public static Placeholders of(Map<String, Object> placeholders) {
        return new Placeholders(convertPlaceholders(placeholders));
    }

    public Placeholders setPlaceholder(String placeholder, Object replacement) {
        this.placeholders.put(placeholder, convertToList(replacement));
        return this;
    }

    public Placeholders removePlaceholder(String placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    private static Map<String, List<String>> convertPlaceholders(Map<String, Object> placeholders) {
        return placeholders.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> convertToList(entry.getValue())
                ));
    }

    private static List<String> convertToList(Object input) {
        if (input instanceof List<?>) {
            return ((List<?>) input).stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }

        if (input instanceof String) {
            return StringUtil.multilineTextToList((String) input);
        }

        return Collections.singletonList(input.toString());
    }


}
