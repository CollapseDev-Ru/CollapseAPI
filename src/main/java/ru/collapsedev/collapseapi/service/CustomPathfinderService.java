package ru.collapsedev.collapseapi.service;

import com.destroystokyo.paper.event.entity.EntityPathfindEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import ru.collapsedev.collapseapi.APILoader;
import ru.collapsedev.collapseapi.api.pathfinder.CustomPathfinder;
import ru.collapsedev.collapseapi.common.pathfinder.CustomPathfinderImpl;

import java.util.ArrayList;
import java.util.List;

public class CustomPathfinderService extends BukkitRunnable implements Listener {


    private static final List<CustomPathfinder> pathfinders = new ArrayList<>();

    public static void addPathfinder(CustomPathfinder pathfinder) {
        pathfinders.add(pathfinder);
    }

    public static void removePathfinder(CustomPathfinder pathfinder) {
        pathfinders.remove(pathfinder);
    }

    public CustomPathfinderService() {
        Bukkit.getPluginManager().registerEvents(this, APILoader.getInstance());
        runTaskTimerAsynchronously(APILoader.getInstance(), 1, 1);
    }

    @Override
    public void run() {
        pathfinders.removeIf(pathfinder -> {
            if (pathfinder.isDead()) {
                return true;
            }
            if (pathfinder.validate() && pathfinder.hasDifference()) {
                pathfinder.stop();
                return pathfinder.isLastUse();
            }
            return false;
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPathfind(EntityPathfindEvent event) {
        Entity entity = event.getEntity();

        if (CustomPathfinderImpl.checkAndUpdateMove(entity, event.getLoc(), true)) {
            event.setCancelled(true);
        }
    }

}
