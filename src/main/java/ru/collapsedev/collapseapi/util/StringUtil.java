package ru.collapsedev.collapseapi.util;

import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@UtilityClass
public class StringUtil {
    public static final Pattern HEX_PATTERN = Pattern.compile("#(?i)[0-9a-f]{6}");
    public static final Pattern LEGACY_COLOR_PATTERN = Pattern.compile("§x(§(?i)[0-9a-f]){6}");

    public String convertHexColor(String text) {
        text = TagParser.GRADIENT.parse(
                text.replaceAll("&#", "#"),
                groups -> StringUtil.applyGradient(
                        groups.get(0), groups.get(1), groups.get(2)
                )
        ).getModifyText();

        return HEX_PATTERN.matcher(text).replaceAll(matchResult -> {
            String group = matchResult.group();
            return applyColorFormatting(group);
        });
    }

    public String applyColorFormatting(String color) {
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

    public String applyGradient(String startColor, String text, String endColor) {
        return asGradient(Color.decode(startColor), text, Color.decode(endColor));
    }

    private String asGradient(Color start, String text, Color end) {
        int length = text.length();
        StringBuilder result = new StringBuilder(length * 14);

        for (int i = 0; i < length; i++) {
            int red = (int) (start.getRed() + (float) (end.getRed() - start.getRed()) / (length - 1) * i);
            int green = (int) (start.getGreen() + (float) (end.getGreen() - start.getGreen()) / (length - 1) * i);
            int blue = (int) (start.getBlue() + (float) (end.getBlue() - start.getBlue()) / (length - 1) * i);

            result.append(applyColorFormatting(
                    String.format("#%02X%02X%02X", red, green, blue)
            )).append(text.charAt(i));
        }
        return result.toString() + ChatColor.RESET;
    }

    private String convertToHex(String text) {
        return LEGACY_COLOR_PATTERN.matcher(text).replaceAll(matchResult
                -> "&#" + stripArgs(matchResult.group(), "§x", "§"));
    }

    public String legacyToHex(String text) {
        return convertToHex(text).replace("§", "&");
    }

    public List<String> legacyToHex(List<String> list) {
        return ObjectUtil.mapList(list, StringUtil::legacyToHex);
    }

    public String stripChar(String text, char character) {
        return text.replaceAll(String.valueOf(character), "");
    }

    public String stripArgs(String text, String... args) {
        for (String arg : args) {
            text = text.replaceAll(arg, "");
        }
        return text;
    }

    public String placeholdersColor(OfflinePlayer offlinePlayer, String text) {
        return color(applyPlaceholders(offlinePlayer, text));
    }

    public List<String> color(List<String> list) {
        return ObjectUtil.mapList(list, StringUtil::color);
    }

    public List<String> placeholdersColor(OfflinePlayer offlinePlayer, List<String> list) {
        return ObjectUtil.mapList(list, line -> placeholdersColor(offlinePlayer, line));
    }


    public String listToString(List<String> lines) {
        List<String> tmpLines = new ArrayList<>(lines);
        tmpLines.replaceAll(s -> s.isEmpty() ? " " : s);
        return String.join("\n", tmpLines);
    }

    public String splitQuote(String quote, String string) {
        return string.split(Pattern.quote(quote + " "))[1];
    }

    public List<String> replaceObjects(List<String> list, Object... replace) {
        return ObjectUtil.mapList(list, line -> replaceObjects(line, replace));
    }

    public String replaceObjects(String text, Object... replace) {
        if (replace.length % 2 != 0) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < replace.length; i += 2) {
            text = text.replace(String.valueOf(replace[i]), String.valueOf(replace[i + 1]));
        }

        return text;
    }


    public String declensions(long point, String[] units, String delimiter) {
        return declensions(true, point, units, delimiter);
    }

    public String declensions(boolean addPoint, long point, String[] units, String delimiter) {
        point = Math.abs(point);
        long last = point % 100;

        StringBuilder builder = new StringBuilder();
        if (addPoint) {
            builder.append(point).append(delimiter);
        }

        if (last > 10 && last < 21) {
            return builder.append(units[2]).toString();
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

        return builder.append(result).toString();
    }


    public String declensions(long point, String[] units) {
        return declensions(point, units, " ");
    }

    public String declensions(long point, String value1, String value2, String value3) {
        return declensions(point, Arrays.asList(value1, value2, value3).toArray(new String[0]));
    }

    public List<String> multilineTextToList(String text) {
        return Arrays.stream(text.split("\n"))
                .map(s -> s.isEmpty() ? " " : s)
                .collect(Collectors.toList());
    }

    public String applyPlaceholders(OfflinePlayer offlinePlayer, String text) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return PlaceholderAPI.setPlaceholders(offlinePlayer, text);
        }
        return text;
    }

    public Map<String, String> parseParameters(String parameters) {
        String[] args = parameters.split(" ");
        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].startsWith("--")) {
                params.put(args[i].substring(2), args[i + 1]);
            }
        }
        return params;
    }


}
