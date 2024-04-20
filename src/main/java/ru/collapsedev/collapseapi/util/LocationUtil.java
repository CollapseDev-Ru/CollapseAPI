package ru.collapsedev.collapseapi.util;

import lombok.experimental.UtilityClass;
import ru.collapsedev.collapseapi.APILoader;
import ru.collapsedev.collapseapi.common.object.Points;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

@UtilityClass
public class LocationUtil {
    public Vector stringToVector(String[] args) {
        double x = Double.parseDouble(args[0]);
        double y = Double.parseDouble(args[1]);
        double z = Double.parseDouble(args[2]);

        return new Vector(x, y, z);
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

    public void setBlock(Location location, MaterialData material) {
        Bukkit.getScheduler().runTask(APILoader.getInstance(), () -> {
            Block block = location.getBlock();
            block.setType(material.getItemType());
//            block.setData(material.getData());
        });

    }

}
