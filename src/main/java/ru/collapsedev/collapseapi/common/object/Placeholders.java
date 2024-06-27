package ru.collapsedev.collapseapi.common.object;

import lombok.Getter;
import ru.collapsedev.collapseapi.util.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class Placeholders {

    public static final Placeholders EMPTY = new Placeholders(Map.of());

    private final Map<String, List<String>> placeholders = new HashMap<>();

    private Placeholders(Map<String, List<String>> placeholders) {
        this.placeholders.putAll(placeholders);
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

    public String apply(String text) {
        for (Map.Entry<String, List<String>> entry : placeholders.entrySet()) {
            text = text.replace(entry.getKey(), StringUtil.listToString(entry.getValue()));
        }
        return text;
    }

    public List<String> apply(List<String> lines) {
        lines = new ArrayList<>(lines);

        for (Map.Entry<String, List<String>> entry : placeholders.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();
            if (values.isEmpty()) {
                values.add("");
            }

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.contains(key)) {
                    lines.set(i, line.replace(key, values.get(0)));
                    for (int j = 1; j < values.size(); j++) {
                        lines.add(i + j, values.get(j));
                    }
                }
            }
        }
        return lines;
    }


}
