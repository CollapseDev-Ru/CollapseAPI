package ru.collapsedev.collapseapi.common.object;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.collapsedev.collapseapi.util.LocationUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@ToString
public class Points implements Cloneable {
    final World world;
    final Vector min, max;

    private Points(World world, Vector min, Vector max) {
        this.world = world;
        this.min = min;
        this.max = max;
    }

    public static Points of(World world, Vector point1, Vector point2) {
        Vector min = LocationUtil.getMinVector(point1, point2);
        Vector max = LocationUtil.getMaxVector(point1, point2);

        return new Points(world, min, max);
    }

    public boolean isInside(Location point) {
        if (world != point.getWorld()) {
            return false;
        }

        int minX = min.getBlockX();
        int minY = min.getBlockY();
        int minZ = min.getBlockZ();

        int maxX = max.getBlockX();
        int maxY = max.getBlockY();
        int maxZ = max.getBlockZ();

        return point.getX() >= minX && point.getX() <= maxX
                && point.getY() >= minY && point.getY() <= maxY
                && point.getZ() >= minZ && point.getZ() <= maxZ;
    }

    public void removeBlocks() {
        int minX = min.getBlockX();
        int minY = min.getBlockY();
        int minZ = min.getBlockZ();

        int maxX = max.getBlockX();
        int maxY = max.getBlockY();
        int maxZ = max.getBlockZ();

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Location location = new Location(world, x, y, z);
                    LocationUtil.setBlock(location, new MaterialData(Material.AIR));
                }
            }
        }
    }

    public Points addRadius(int size) {
        min.add(new Vector(-size, -size, -size));
        max.add(new Vector(size, size, size));
        return this;
    }

    @Override
    public Points clone() {
        return Points.of(world, min, max);
    }
}
