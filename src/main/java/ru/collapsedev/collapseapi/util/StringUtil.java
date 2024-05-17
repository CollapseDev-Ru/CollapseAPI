package ru.collapsedev.collapseapi.util;

import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import ru.collapsedev.collapseapi.APILoader;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@UtilityClass
public class StringUtil {
    private static final Pattern HEX_PATTERN = Pattern.compile("(&|)#(?i)[0-9a-f]{6}");

    private String convertHexColor(String text) {
        return HEX_PATTERN.matcher(text).replaceAll(matchResult -> {
            String color = stripChar(matchResult.group(), '&');
            return applyColorFormatting(color);
        });
    }

    private String applyColorFormatting(String color) {
        return "§x" + color.substring(1)
                .chars().mapToObj(c -> "§" + (char) c)
                .collect(Collectors.joining());
    }

    public String color(String text) {
        text = ChatColor.translateAlternateColorCodes('&', text);

        if (VersionUtil.getBukkitVersion() >= 16) {
            text = convertHexColor(text);
        }
        return text;
    }

    public String placeholdersColor(OfflinePlayer offlinePlayer, String text) {
        return color(applyPlaceholders(offlinePlayer, text));
    }

    public List<String> color(List<String> list) {
        return mapList(list, StringUtil::color);
    }

    public List<String> placeholdersColor(OfflinePlayer offlinePlayer, List<String> list) {
        return mapList(list, line -> placeholdersColor(offlinePlayer, line));
    }

    public String stripChar(String text, char character) {
        return text.replace(String.valueOf(character), "");
    }

    public String getOrDefault(String value, String defaultValue) {
        return value != null ? value : defaultValue;
    }

    public String listToString(List<String> lines) {
        lines.replaceAll(s -> s.isEmpty() ? " " : s);
        return String.join("\n", lines);
    }

    public String splitQuote(String quote, String string) {
        return string.split(Pattern.quote(quote + " "))[1];
    }

    public List<String> replaceObjects(List<String> list, Object... replace) {
        return mapList(list, line -> replaceObjects(line, replace));
    }

    public String replaceObjects(String text, Object... replace) {
        if (replace.length % 2 != 0) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < replace.length; i += 2) {
            text = text.replace(String.valueOf(replace[i]), String.valueOf(replace[i + 1]));
        }

        return StringUtil.color(text);
    }

    public List<String> mapList(List<String> list, Function<String, String> mapper) {
        return list.stream().map(mapper).collect(Collectors.toList());
    }

    public String declensions(long point, String[] units, String delimiter) {
        point = Math.abs(point);
        long last = point % 100;

        if (last > 10 && last < 21) {
            return point + delimiter + units[2];
        }

        last = point % 10;

        String result;
        if (last == 1) {
            result = units[0];
        } else if (last > 1 && last < 5) {
            result = units[1];
        } else {
            result = units[2];
        }

        return point + delimiter + result;
    }


    public String declensions(long point, String[] units) {
        return declensions(point, units, " ");
    }

    public String declensions(long point, String value1, String value2, String value3) {
        return declensions(point, Arrays.asList(value1, value2, value3).toArray(new String[0]));
    }

    public String setCustomPlaceholders(String text, Map<String, List<String>> placeholders) {
        for (Map.Entry<String, List<String>> entry : placeholders.entrySet()) {
            text = text.replaceAll("{" + entry.getKey() + "}", listToString(entry.getValue()));
        }
        return text;
    }

    public List<String> setCustomPlaceholders(List<String> lines, Map<String, List<String>> placeholders) {
        lines = new ArrayList<>(lines);

        for (Map.Entry<String, List<String>> entry : placeholders.entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.contains(key)) {
                    lines.set(i, line.replace("{" + key + "}", value.get(0)));
                    for (int j = 1; j < value.size(); j++) {
                        lines.add(i + j, value.get(j));
                    }
                }
            }
        }
        return lines;
    }

    public List<String> multilineTextToList(String text) {
        return Arrays.stream(text.split("\n")).collect(Collectors.toList());
    }

    public String applyPlaceholders(OfflinePlayer offlinePlayer, String text) {
        if (APILoader.getInstance().isEnabledPlaceholderAPI()) {
            return PlaceholderAPI.setPlaceholders(offlinePlayer, text);
        }
        return text;
    }


}
