package ru.collapsedev.collapseapi.common.object;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;

@Getter
@AllArgsConstructor
public enum ColorEnum {
    BLACK(Color.BLACK),
    BLUE(Color.BLUE),
    CYAN(Color.CYAN),
    DARK_GRAY(Color.DARK_GRAY),
    GRAY(Color.GRAY),
    GREEN(Color.GREEN),
    LIGHT_GRAY(Color.LIGHT_GRAY),
    MAGENTA(Color.MAGENTA),
    ORANGE(Color.ORANGE),
    PINK(Color.PINK),
    RED(Color.RED),
    WHITE(Color.WHITE),
    YELLOW(Color.YELLOW);

    private final Color color;

    public static Color getColorByName(String colorName) {
        try {
            return ColorEnum.valueOf(colorName.toUpperCase()).getColor();
        } catch (IllegalArgumentException e) {
            return Color.BLACK;
        }
    }
}
