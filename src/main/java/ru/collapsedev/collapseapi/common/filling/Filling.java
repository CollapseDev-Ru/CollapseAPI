package ru.collapsedev.collapseapi.common.filling;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import ru.collapsedev.collapseapi.common.object.Points;
import ru.collapsedev.collapseapi.util.LocationUtil;
import org.bukkit.Location;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Consumer;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;


@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Filling {
    final Plugin plugin;
    final Points points;
    final Function<Location, MaterialData> function;
    final long delay;
    final int sleepInterval;

    boolean generate;

    BukkitRunnable bukkitRunnable;

    public Filling(Plugin plugin, Points points, Function<Location, MaterialData> function, long delay, int sleepInterval) {
        this.plugin = plugin;
        this.points = points;
        this.function = function;
        this.delay = delay;
        this.sleepInterval = sleepInterval;
    }

    @SneakyThrows
    public void start(Class<? extends AbstractFilling> clazz) {
        AbstractFilling generator = clazz.getConstructor(Points.class).newInstance(points);

        generate = true;
        bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                fill(generator);
            }
        };
        bukkitRunnable.runTaskAsynchronously(plugin);
    }

    @SneakyThrows
    private void fill(AbstractFilling generator) {
        AtomicInteger atomicInteger = new AtomicInteger();
        Consumer<Location> consumer = location -> {

            MaterialData material = function.apply(location);
            LocationUtil.setBlock(location, material);

            boolean isSleep = atomicInteger.incrementAndGet() % sleepInterval == 0;
            sleep(isSleep);
        };


        generator.start(consumer);
        generate = false;
    }

    @SneakyThrows
    private void sleep(boolean sleep) {
        if (sleep) {
            Thread.sleep(delay);
        }
    }
    public void kill() {
        bukkitRunnable.cancel();
    }

}
