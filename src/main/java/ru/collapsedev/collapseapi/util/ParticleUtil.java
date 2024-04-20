package ru.collapsedev.collapseapi.util;

import ru.collapsedev.collapseapi.common.object.ParticleColor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParticleUtil {
    private static final Map<Character, ParticleColor> colorList = new HashMap<>() {{
        put('0', ParticleColor.of(1.0E-4, 1.0E-4, 1.0E-4));
        put('1', ParticleColor.of(1.0E-4, 1.0E-4, 1.0));
        put('2', ParticleColor.of(0.1333, 0.545, 0.1333));
        put('3', ParticleColor.of(0.1255, 0.698, 0.6666));
        put('4', ParticleColor.of(1.0, 1.0E-4, 1.0E-4));
        put('5', ParticleColor.of(0.5804, 1.0E-4, 0.8274));
        put('6', ParticleColor.of(1.0, 0.8431, 1.0E-4));
        put('7', ParticleColor.of(0.8118, 0.8118, 0.8118));
        put('8', ParticleColor.of(0.4118, 0.4118, 0.4118));
        put('9', ParticleColor.of(0.4118, 1.0, 1.0));
        put('a', ParticleColor.of(1.0E-4, 1.0, 1.0E-4));
        put('b', ParticleColor.of(1.0E-4, 1.0, 1.0));
        put('c', ParticleColor.of(0.8039, 0.3608, 0.3608));
        put('d', ParticleColor.of(1.0, 0.4118, 0.7059));
        put('e', ParticleColor.of(1.0, 1.0, 1.0E-4));
        put('f', ParticleColor.of(1.0, 1.0, 1.0));
    }};
    
    public ParticleColor getParticleColor(ChatColor color) {
        return colorList.get(color.getChar());
    }


    public void sendColorizeParticle(Player player, Location location, ParticleColor color) {
        player.spawnParticle(Particle.REDSTONE, location.getX(), location.getY(), location.getZ(), 0, color.getRed(), color.getGreen(), color.getBlue());
    }
    public void spawnColorizeParticle(Location location, ParticleColor color) {
        World world = location.getWorld();
        world.spawnParticle(Particle.REDSTONE, location.getX(), location.getY(), location.getZ(), 0, color.getRed(), color.getGreen(), color.getBlue());
    }


    public void sendColorizeParticles(Player player, List<Location> locations, ParticleColor color) {
        locations.forEach(location -> sendColorizeParticle(player, location, color));
    }
    public void spawnColorizeParticles(List<Location> locations, ParticleColor color) {
        locations.forEach(location -> spawnColorizeParticle(location, color));
    }


    public void sendAsyncColorizeParticle(Player player, Location location, ParticleColor color) {
        BukkitUtil.runAsync(() -> sendColorizeParticle(player, location, color));
    }
    public void spawnAsyncColorizeParticle(Location location, ParticleColor color) {
        BukkitUtil.runAsync(() -> spawnColorizeParticle(location, color));
    }


    public void sendAsyncColorizeParticles(Player player, List<Location> locations, ParticleColor color) {
        BukkitUtil.runAsync(() -> sendColorizeParticles(player, locations, color));
    }
    public void spawnAsyncColorizeParticles(List<Location> locations, ParticleColor color) {
        BukkitUtil.runAsync(() -> spawnColorizeParticles(locations, color));
    }
}
