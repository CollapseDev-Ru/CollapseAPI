package ru.collapsedev.collapseapi.common.pathfinder;

import com.destroystokyo.paper.entity.Pathfinder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import ru.collapsedev.collapseapi.api.pathfinder.CustomPathfinder;
import ru.collapsedev.collapseapi.service.CustomPathfinderService;
import ru.collapsedev.collapseapi.util.BukkitUtil;
import ru.collapsedev.collapseapi.util.ObjectUtil;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomPathfinderImpl implements CustomPathfinder {

    private static final String META_KEY = "custom-pathfind";
    private static final int DIFFERENCE = 1;

    Plugin plugin;
    Entity entity;
    Location target;
    double speed;

    Pathfinder pathfinder;

    private CustomPathfinderImpl(Plugin plugin, Entity entity, Location target, double speed) {
        this.plugin = plugin;
        this.entity = entity;
        this.target = target;
        this.speed = speed;

        this.pathfinder = ((Mob) entity).getPathfinder();

        CustomPathfinderService.addPathfinder(this);
    }

    public static CustomPathfinder create(Plugin plugin, Entity entity, Location target, double speed) {
        return new CustomPathfinderImpl(plugin, entity, target.getBlock().getLocation(), speed);
    }

    public static CustomPathfinder parse(Entity entity) {
        if (!validate(entity)) {
            return null;
        }

        return ObjectUtil.castValue(
                entity.getMetadata(META_KEY)
                        .get(0)
                        .value()
        );
    }

    public static boolean validate(Entity entity) {
        return entity.hasMetadata(META_KEY);
    }

    @Override
    public boolean validate() {
        return validate(entity);
    }

    @Override
    public boolean isDead() {
        return !entity.isValid() && entity.isDead();
    }

    @Override
    public void move() {
        move(20);
    }

    @Override
    public void move(int startDelay) {
        BukkitUtil.runLaterSync(plugin, () -> {
            Pathfinder.PathResult path = pathfinder.findPath(target);
            if (path != null) {
                if (!validate()) {
                    entity.setMetadata(META_KEY, new FixedMetadataValue(plugin, this));
                }
                pathfinder.moveTo(path, speed);
            }
        }, startDelay);
    }

    @Override
    public boolean isAtTargetLocation(Location location) {
        return target.equals(location);
    }

    public static boolean checkAndUpdateMove(Entity entity, Location to, boolean update) {
        CustomPathfinder customPathfinder = parse(entity);
        if (customPathfinder == null) {
            return false;
        }

        boolean notAtTargetLocation = !customPathfinder.isAtTargetLocation(to);
        if (notAtTargetLocation && update) {
            BukkitUtil.runSync(customPathfinder.getPlugin(), customPathfinder::move);
        }

        return notAtTargetLocation;
    }

    @Override
    public boolean hasDifference() {
       return entity.getLocation().distance(target) <= DIFFERENCE;
    }

    @Override
    public void stop() {
        entity.removeMetadata(META_KEY, plugin);
        pathfinder.stopPathfinding();
    }

    @Override
    public void remove() {
        stop();
        CustomPathfinderService.removePathfinder(this);
    }


}
