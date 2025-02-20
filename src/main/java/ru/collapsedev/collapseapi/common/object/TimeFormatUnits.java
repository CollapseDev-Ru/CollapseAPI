package ru.collapsedev.collapseapi.common.object;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TimeFormatUnits {
    FULL(new String[][]{
            {" день", " дня", " дней"},
            {" час", " часа", " часов"},
            {" минуту", " минуты", " минут"},
            {" секунду", " секунды", " секунд"},
            {" миллисекунду", " миллисекунды", " миллисекунд"}
    }),
    SHORT(new String[][]{
            {" дн.", " час.", " мин.", " сек.", " мс."}
    }),
    VERY_SHORT(new String[][]{
            {"д.", "ч.", "м.", "с.", "мс."}
    });

    private final String[][] units;

    public static TimeFormatUnits getByName(String name) {
        try {
            return TimeFormatUnits.valueOf(
                    name.toUpperCase().replace("-", "_")
            );
        } catch (IllegalArgumentException e) {
            return FULL;
        }
    }
}
