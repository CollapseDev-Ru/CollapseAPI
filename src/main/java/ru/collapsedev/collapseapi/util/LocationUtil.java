package ru.collapsedev.collapseapi.util;

import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import ru.collapsedev.collapseapi.APILoader;
import ru.collapsedev.collapseapi.common.object.Pair;
import ru.collapsedev.collapseapi.common.object.Points;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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
        return new Vector(
                Math.min(point1.getBlockX(), point2.getBlockX()),
                Math.min(point1.getBlockY(), point2.getBlockY()),
                Math.min(point1.getBlockZ(), point2.getBlockZ())
        );
    }

    public Vector getMaxVector(Vector point1, Vector point2) {
        return new Vector(
                Math.max(point1.getBlockX(), point2.getBlockX()),
                Math.max(point1.getBlockY(), point2.getBlockY()),
                Math.max(point1.getBlockZ(), point2.getBlockZ())
        );
    }

    public Location vectorToLocation(World world, Vector vector) {
        return new Location(world, vector.getX(), vector.getY(), vector.getZ());
    }

    public void setBlock(Location location, XMaterial material) {
        XBlock.setType(location.getBlock(), material);
    }

    public Location getRandomLocationInVector(Location location, Vector vector) {
        return location.clone().add(
                RandomUtil.randomDoubleMulti(vector.getX()),
                RandomUtil.randomDoubleMulti(vector.getY()),
                RandomUtil.randomDoubleMulti(vector.getZ())
        );
    }

    public Location getRandomLocationInRadius(Location location, int radius) {
        Location to = getRandomLocationInRadiusNotY(location, radius);
        return to.add(0, RandomUtil.randomDoubleMulti(radius), 0);
    }

    public Location getRandomLocationInRadiusHG(Location location, int radius, int difference) {
        Location to = getRandomLocationInRadiusNotY(location, radius);

        if (Math.abs(to.getY() - location.getY()) > difference) {
            return location;
        }

        to.setY(to.getWorld().getHighestBlockAt(to).getY() + 1);
        return to;
    }

    public Location getRandomLocationInRadiusNotY(Location location, int radius) {
        return location.clone().add(
                RandomUtil.randomDoubleMulti(radius),
                0,
                RandomUtil.randomDoubleMulti(radius)
        );
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

    public Player getRandomPlayer(Location location, int radius) {
        List<Player> players = getRandomPlayers(location, radius, 1);
        return !players.isEmpty() ? players.get(0) : null;
    }

    public List<Player> getRandomPlayers(Location location, int radius, int limit) {
        return location.getNearbyPlayers(radius).stream()
                .sorted((p1, p2) -> RandomUtil.randomInt(100))
                .limit(limit)
                .collect(Collectors.toList());
    }

}
