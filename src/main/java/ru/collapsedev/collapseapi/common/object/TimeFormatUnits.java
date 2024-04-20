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
            {" секунду", " секунды", " секунд"}
    }),
    SHORT(new String[][]{
            {" дн.", " час.", " мин.", " сек."}
    }),
    VERY_SHORT(new String[][]{
            {"д.", "ч.", "м.", "с."}
    });

    private final String[][] units;
}
