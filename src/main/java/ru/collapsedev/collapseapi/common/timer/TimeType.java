package ru.collapsedev.collapseapi.common.timer;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum TimeType {
    BEFORE,
    RIGHT;

    private long before = -1;

    public TimeType setBefore(long before) {
        this.before = before;
        return this;
    }
}