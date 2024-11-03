package ru.collapsedev.collapseapi.common.timer;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.collapsedev.collapseapi.common.object.MapAccessor;
import ru.collapsedev.collapseapi.common.object.TimeUnit;
import ru.collapsedev.collapseapi.util.ObjectUtil;
import ru.collapsedev.collapseapi.util.TimeUtil;

import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduledEvent {

    @Setter
    ZoneId zoneId = ZoneId.of("Europe/Moscow");

    final TimeData timeData;
    final AtomicLong counter;

    long rightTime;
    Set<Long> beforeTimes;

    long leftTime;
    Consumer<TimeType> action = o -> {};


    public ScheduledEvent(String time, List<Long> before) {
        this.timeData = TimeData.of(time, before);
        this.counter = new AtomicLong();

        nextTime();
    }

    public static void setActions(Set<ScheduledEvent> events, Consumer<TimeType> action) {
        events.forEach(event -> event.onAction(action));
    }

    public static Set<ScheduledEvent> of(MapAccessor accessor) {
        List<Long> before = ObjectUtil.mapList(
                accessor.getStringList("before"),
                time -> TimeUtil.parseTime(time, TimeUnit.SECONDS)
        );

        String zoneId = accessor.getString("zone-id");

        return ObjectUtil.mapSet(
                new HashSet<>(accessor.getStringList("time")),
                time -> {
                    ScheduledEvent scheduledEvent = new ScheduledEvent(time, before);
                    if (zoneId != null) {
                        scheduledEvent.setZoneId(ZoneId.of(zoneId));
                    }
                    return scheduledEvent;
                }
        );
    }

    public void nextTime() {
        this.rightTime = TimeUtil.calculateDesiredTime(
                timeData.getTime(),
                zoneId,
                ChronoUnit.SECONDS
        ) + 2;

        this.beforeTimes = timeData.getBefore()
                .stream()
                .map(before -> rightTime - before)
                .filter(before -> before > 0)
                .collect(Collectors.toSet());
    }


    public void tick() {
        long count = counter.incrementAndGet();
        leftTime = rightTime - count;

        if (beforeTimes.contains(count)) {
            action.accept(TimeType.BEFORE.setBefore(leftTime));
        } else if (leftTime == 0) {
            action.accept(TimeType.RIGHT);
            update();
        }
    }

    private void update() {
        nextTime();
        counter.set(0);
    }

    public void onAction(Consumer<TimeType> action) {
        this.action = action;
    }

}
