package ru.collapsedev.collapseapi.util;

import lombok.experimental.UtilityClass;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class ColorUtil {

    private static final Map<String, Color> colorMap = new HashMap<>();

    static {
        colorMap.put("BLACK", Color.BLACK);
        colorMap.put("BLUE", Color.BLUE);
        colorMap.put("CYAN", Color.CYAN);
        colorMap.put("DARK_GRAY", Color.DARK_GRAY);
        colorMap.put("GRAY", Color.GRAY);
        colorMap.put("GREEN", Color.GREEN);
        colorMap.put("LIGHT_GRAY", Color.LIGHT_GRAY);
        colorMap.put("MAGENTA", Color.MAGENTA);
        colorMap.put("ORANGE", Color.ORANGE);
        colorMap.put("PINK", Color.PINK);
        colorMap.put("RED", Color.RED);
        colorMap.put("WHITE", Color.WHITE);
        colorMap.put("YELLOW", Color.YELLOW);
    }

    public Color getColor(String color) {
        return colorMap.getOrDefault(color.toUpperCase(), Color.BLACK);
    }
}