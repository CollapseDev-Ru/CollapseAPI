package ru.collapsedev.collapseapi.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@UtilityClass
public class StringUtil {

    public String color(String text) {
        text = ChatColor.translateAlternateColorCodes('&', text);

//        int version = Integer.parseInt(Bukkit.getBukkitVersion().split("\\.")[1]);
//        if (version >= 16)
//            text = text.replaceAll("#([0-9a-fA-F]{6})", "§x§$1");

        return text;
    }

    public static String splitQuote(String quote, String string) {
        return string.split(Pattern.quote(String.format("%s ", quote)))[1];
    }
    public List<String> color(List<String> list) {
        return mapList(list, StringUtil::color);
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
        String result = (last == 0 || last > 4) ? units[2] : (last == 1) ? units[0] : units[1];

        return point + delimiter + result;
    }
    public String declensions(long point, String[] units) {
        return declensions(point, units, " ");
    }

    public String declensions(long point, String value1, String value2, String value3) {
        return declensions(point, Arrays.asList(value1, value2, value3).toArray(new String[0]));
    }

}
