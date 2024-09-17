package ru.collapsedev.collapseapi.common.object;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeUnit {
    YEARS(TimeUnit.YEAR_SCALE),
    DAYS(TimeUnit.DAY_SCALE),
    HOURS(TimeUnit.HOUR_SCALE),
    MINUTES(TimeUnit.MINUTE_SCALE),
    SECONDS(TimeUnit.SECOND_SCALE),
    TICKS(TimeUnit.TICK_SCALE),
    MILLISECONDS(TimeUnit.MILLI_SCALE);

    private static final long MILLI_SCALE = 1L;
    private static final long TICK_SCALE = 50L;
    private static final long SECOND_SCALE = 1000L;
    private static final long MINUTE_SCALE = 60L * SECOND_SCALE;
    private static final long HOUR_SCALE = 60L * MINUTE_SCALE;
    private static final long DAY_SCALE = 24L * HOUR_SCALE;
    private static final long YEAR_SCALE = 365L * DAY_SCALE;

    private final long time;

}