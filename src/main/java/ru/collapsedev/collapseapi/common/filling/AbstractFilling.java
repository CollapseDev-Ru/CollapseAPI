package ru.collapsedev.collapseapi.common.filling;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ru.collapsedev.collapseapi.common.object.Points;
import org.bukkit.Location;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;


@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public abstract class AbstractFilling {
    Points points;

    int minX;
    int minY;
    int minZ;

    int maxX;
    int maxY;
    int maxZ;

    protected AbstractFilling(Points points) {
        this.points = points;

        Vector min = points.getMin();
        Vector max = points.getMax();

        this.minX = min.getBlockX();
        this.minY = min.getBlockY();
        this.minZ = min.getBlockZ();

        this.maxX = max.getBlockX();
        this.maxY = max.getBlockY();
        this.maxZ = max.getBlockZ();

    }

    public abstract void start(Consumer<Location> consumer);
}
