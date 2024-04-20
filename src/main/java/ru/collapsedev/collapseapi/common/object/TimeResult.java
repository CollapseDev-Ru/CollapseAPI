package ru.collapsedev.collapseapi.common.object;

import lombok.Getter;

@Getter
public enum TimeResult {
    YEARS(TimeResult.YEAR_SCALE),
    DAYS(TimeResult.DAY_SCALE),
    HOURS(TimeResult.HOUR_SCALE),
    MINUTES(TimeResult.MINUTE_SCALE),
    SECONDS(TimeResult.SECOND_SCALE),
    TICKS(TimeResult.TICK_SCALE),
    MILLISECONDS(TimeResult.MILLI_SCALE);

    public static final long MILLI_SCALE = 1L;
    public static final long TICK_SCALE = 50L;
    public static final long SECOND_SCALE = 1000L;
    public static final long MINUTE_SCALE = 60L * SECOND_SCALE;
    public static final long HOUR_SCALE = 60L * MINUTE_SCALE;
    public static final long DAY_SCALE = 24L * HOUR_SCALE;
    public static final long YEAR_SCALE = 365L * DAY_SCALE;

    private final long time;

    TimeResult(long time) {
        this.time = time;
    }
}