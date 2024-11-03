package ru.collapsedev.collapseapi.common.object;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ru.collapsedev.collapseapi.util.RandomUtil;
import ru.collapsedev.collapseapi.util.TimeUtil;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RandomTime {

    long left;
    long right;

    public static RandomTime of(String timeRange, TimeUnit unit) {
        String[] times = timeRange.split("-");

        long left = TimeUtil.parseTime(times[0], unit);
        long right = times.length == 2 ? TimeUtil.parseTime(times[1], unit) : left;

        return new RandomTime(
                (int) Math.min(left, right),
                (int) Math.max(left, right)
        );
    }

    public static RandomTime of(String timeRange) {
        return of(timeRange, TimeUnit.SECONDS);
    }

    public int get() {
        if (left == right) {
            return (int) left;
        }

        return RandomUtil.randomInt((int) left, (int) right + 1);
    }
}
