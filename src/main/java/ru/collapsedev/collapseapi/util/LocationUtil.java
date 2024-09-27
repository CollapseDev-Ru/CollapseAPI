package ru.collapsedev.collapseapi.util;

import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import ru.collapsedev.collapseapi.APILoader;
import ru.collapsedev.collapseapi.common.object.Pair;
import ru.collapsedev.collapseapi.common.object.Points;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class LocationUtil {

    public static final String DELIMITER = ":";

    @Getter
    private static final Vector emptyVector = new Vector(0, 0, 0);
    @Getter
    private static final Location emptyLocation = new Location(null, 0, 0, 0);

    public Vector stringToVector(String[] args) {
        double x = Double.parseDouble(args[0]);
        double y = Double.parseDouble(args[1]);
        double z = Double.parseDouble(args[2]);

        return new Vector(x, y, z);
    }

    public Vector stringToVector(String vector) {
        return stringToVector(vector.split(DELIMITER));
    }

    public Location stringToLocation(String[] args) {
        World world = Bukkit.getWorld(args[0]);

        double x = Double.parseDouble(args[1]);
        double y = Double.parseDouble(args[2]);
        double z = Double.parseDouble(args[3]);

        boolean hasDirection = args.length == 6;
        float yaw = hasDirection ? Float.parseFloat(args[4]) : 0;
        float pitch = hasDirection ? Float.parseFloat(args[5]) : 0;

        return new Location(world, x, y, z, yaw, pitch);
    }

    public Location stringToLocation(String location) {
        return stringToLocation(location.split(DELIMITER));
    }

    public Vector getMinVector(Vector point1, Vector point2) {
        int minX = Math.min(point1.getBlockX(), point2.getBlockX());
        int minY = Math.min(point1.getBlockY(), point2.getBlockY());
        int minZ = Math.min(point1.getBlockZ(), point2.getBlockZ());

        return new Vector(minX, minY, minZ);
    }

    public Vector getMaxVector(Vector point1, Vector point2) {
        int maxX = Math.max(point1.getBlockX(), point2.getBlockX());
        int maxY = Math.max(point1.getBlockY(), point2.getBlockY());
        int maxZ = Math.max(point1.getBlockZ(), point2.getBlockZ());

        return new Vector(maxX, maxY, maxZ);
    }

    public Location vectorToLocation(World world, Vector vector) {
        return new Location(world, vector.getX(), vector.getY(), vector.getZ());
    }

    public void setBlock(Location location, XMaterial material) {
        XBlock.setType(location.getBlock(), material);
    }

    public Location getRandomLocationInVector(Location location, Vector vector) {
        double x = RandomUtil.randomDoubleMulti(vector.getX());
        double y = RandomUtil.randomDoubleMulti(vector.getY());
        double z = RandomUtil.randomDoubleMulti(vector.getZ());

        return location.clone().add(x, y, z);
    }

    public Location getRandomLocationInRadius(Location location, int radius) {
        double value = RandomUtil.randomDoubleMulti(radius);

        return location.clone().add(value, value, value);
    }
    public Location getRandomLocationInRadiusNotY(Location location, int radius) {
        double value = RandomUtil.randomDoubleMulti(radius);

        return location.clone().add(value, 0, value);
    }

    public String locationToSting(Location location, String delimiter) {
        return location.getWorld().getName() + delimiter +
                location.getX() + delimiter +
                location.getY() + delimiter +
                location.getZ();
    }

    public String locationYawToSting(Location location, String delimiter) {
        return locationToSting(location, delimiter)
                + delimiter + location.getYaw()
                + delimiter + location.getPitch();
    }

    public String locationToSting(Location location) {
        return locationToSting(location, DELIMITER);
    }

    public String locationYawToSting(Location location) {
        return locationYawToSting(location, DELIMITER);
    }

}
