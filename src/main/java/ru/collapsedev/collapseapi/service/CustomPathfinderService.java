package ru.collapsedev.collapseapi.service;

import org.bukkit.scheduler.BukkitRunnable;
import ru.collapsedev.collapseapi.APILoader;
import ru.collapsedev.collapseapi.api.pathfinder.CustomPathfinder;

import java.util.ArrayList;
import java.util.List;

public class CustomPathfinderService extends BukkitRunnable {

    private static final List<CustomPathfinder> pathfinders = new ArrayList<>();

    public static void addPathfinder(CustomPathfinder pathfinder) {
        pathfinders.add(pathfinder);
    }

    public static void removePathfinder(CustomPathfinder pathfinder) {
        pathfinders.remove(pathfinder);
    }

    public CustomPathfinderService() {
        runTaskTimerAsynchronously(APILoader.getInstance(), 1, 1);
    }

    @Override
    public void run() {
        pathfinders.removeIf(CustomPathfinder::isDead);
        pathfinders.parallelStream()
                .filter(CustomPathfinder::validate)
                .filter(CustomPathfinder::hasDifference)
                .forEach(CustomPathfinder::stop);
    }
}
