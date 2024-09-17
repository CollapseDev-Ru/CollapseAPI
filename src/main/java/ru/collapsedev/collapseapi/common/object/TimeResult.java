package ru.collapsedev.collapseapi.common.object;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@Deprecated
@AllArgsConstructor
public enum TimeResult {
    YEARS(TimeUnit.YEARS),
    DAYS(TimeUnit.DAYS),
    HOURS(TimeUnit.HOURS),
    MINUTES(TimeUnit.MINUTES),
    SECONDS(TimeUnit.SECONDS),
    TICKS(TimeUnit.TICKS),
    MILLISECONDS(TimeUnit.MILLISECONDS);

    private final TimeUnit unit;
}