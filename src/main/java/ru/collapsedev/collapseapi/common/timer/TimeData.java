package ru.collapsedev.collapseapi.common.timer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@AllArgsConstructor(staticName = "of")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimeData {
    String time;
    List<Long> before;
}