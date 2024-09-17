package ru.collapsedev.collapseapi.util;

import lombok.experimental.UtilityClass;
import ru.collapsedev.collapseapi.common.object.TimeFormatUnits;
import ru.collapsedev.collapseapi.common.object.TimeResult;
import ru.collapsedev.collapseapi.common.object.TimeUnit;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class TimeUtil {

    @Deprecated
    public long parseTime(String input, TimeResult result) {
        return parseTime(input, result.getUnit());
    }

    public long parseTime(String input, TimeUnit result) {
        Pattern pattern = Pattern.compile("(\\d+)([tsmhdy]?)");
        Matcher matcher = pattern.matcher(input);

        long sum = matcher.results()
                .mapToLong(match -> {
                    long value = Integer.parseInt(match.group(1));

                    String group = match.group(2);
                    char unit = group.isEmpty() ? '?' : group.charAt(0);

                    return value * getMillisecondsByUnit(unit);
                })
                .sum();

        return sum / result.getTime();
    }

    private long getMillisecondsByUnit(char unit) {

        switch (unit) {
            case 's': return TimeUnit.SECONDS.getTime();
            case 'm': return TimeUnit.MINUTES.getTime();
            case 'h': return TimeUnit.HOURS.getTime();
            case 'd': return TimeUnit.DAYS.getTime();
            case 'y': return TimeUnit.YEARS.getTime();
            case 't': return TimeUnit.TICKS.getTime();
            default: return TimeUnit.MILLISECONDS.getTime();
        }
    }


    public String formatTime(long seconds, TimeFormatUnits timeFormat) {
        return formatTime(seconds, TimeUnit.SECONDS, timeFormat);
    }

    public String formatTime(long time, TimeUnit inputTimeUnit, TimeFormatUnits timeFormat) {
        if (time == -1) {
            return "Никогда";
        }

        String[][] units = timeFormat.getUnits();
        long millis = time * inputTimeUnit.getTime();

        switch (timeFormat) {
            case FULL: return formatTimeFull(millis, units);
            case SHORT:
            case VERY_SHORT: return formatTimeShort(millis, units);
            default: throw new RuntimeException();
        }
    }

    private String formatTimeFull(long millis, String[][] units) {
        if (millis == 0) {
            return "0" + units[3][2];
        }
        if (1000 > millis) {
            return StringUtil.declensions(millis, units[4], "");
        }

        long[] time = splitTime(millis / 1000);

        return IntStream.range(0, time.length)
                .filter(i -> time[i] > 0)
                .mapToObj(i -> StringUtil.declensions(time[i], units[i], ""))
                .collect(Collectors.joining(" "));
    }

    private String formatTimeShort(long millis, String[][] units) {
        if (millis == 0) {
            return "0" + units[0][3];
        }
        if (1000 > millis) {
            return millis + units[0][4];
        }

        long[] time = splitTime(millis / 1000);

        return IntStream.range(0, time.length)
                .filter(i -> time[i] > 0)
                .mapToObj(i -> time[i] + units[0][i])
                .collect(Collectors.joining(" "));
    }

    private long[] splitTime(long seconds) {
        return new long[]{
                seconds / 86400,
                seconds / 3600 % 24,
                seconds / 60 % 60,
                seconds % 60
        };
    }



    public LocalDateTime desiredDateTime(LocalDateTime currentDate, String updateTime) {
        LocalTime desiredTime = LocalTime.parse(updateTime);
        return currentDate.with(desiredTime);
    }

    public LocalDateTime getCurrentDateTimeInZone(String zoneId) {
        return LocalDateTime.now(ZoneId.of(zoneId));
    }

    public LocalDateTime adjustDesiredDate(LocalDateTime currentDate, LocalDateTime desiredDate) {
        return desiredDate.plusDays(currentDate.isAfter(desiredDate) ? 1 : 0);
    }

    public long getDifferenceTime(LocalDateTime current, LocalDateTime desired, ChronoUnit unit) {
        return unit.between(current, desired);
    }

    public long calculateDesiredTime(String time, ZoneId zoneId, ChronoUnit unit) {
        LocalDateTime currentDate = getCurrentDateTimeInZone(zoneId.toString());
        LocalDateTime desiredDate = desiredDateTime(currentDate, time);

        LocalDateTime localDateTime = adjustDesiredDate(currentDate, desiredDate);

        return getDifferenceTime(currentDate, localDateTime, unit);
    }
}
