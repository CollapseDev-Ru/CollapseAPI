package ru.collapsedev.collapseapi.common.timer;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AbstractTimer extends BukkitRunnable {

    @Setter
    Set<ScheduledEvent> events = new HashSet<>();
    @Getter
    ScheduledEvent leftEvent;

    @Setter
    Runnable ticker;

    public AbstractTimer(Plugin plugin) {
        runTaskTimerAsynchronously(plugin, 20, 20);
    }

    public void addEvent(ScheduledEvent event) {
        events.add(event);
    }

    @Override
    public void run() {
        AtomicLong leftTime = new AtomicLong(-1);
        events.forEach(event -> {
            event.tick();
            long to = event.getLeftTime();
            if (leftTime.get() == -1 || leftTime.get() >= to) {
                leftTime.set(to);
                leftEvent = event;
            }
        });

        if (ticker != null) {
            ticker.run();
        }
    }
}