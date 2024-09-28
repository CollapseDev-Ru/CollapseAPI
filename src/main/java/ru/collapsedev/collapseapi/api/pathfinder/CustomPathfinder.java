package ru.collapsedev.collapseapi.api.pathfinder;

import com.destroystokyo.paper.entity.Pathfinder;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

public interface CustomPathfinder {

    Plugin getPlugin();
    Entity getEntity();
    Location getTarget();
    double getSpeed();

    Pathfinder getPathfinder();

    boolean isDead();
    boolean validate();
    void move();
    void move(int startDelay);
    void stop();
    void remove();

    boolean isAtTargetLocation(Location location);
    boolean hasDifference();

}
