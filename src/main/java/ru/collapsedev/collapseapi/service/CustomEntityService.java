package ru.collapsedev.collapseapi.service;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import ru.collapsedev.collapseapi.APILoader;
import ru.collapsedev.collapseapi.api.entity.CustomEntity;
import ru.collapsedev.collapseapi.api.pathfinder.CustomPathfinder;
import ru.collapsedev.collapseapi.common.entity.CustomEntityImpl;
import ru.collapsedev.collapseapi.common.pathfinder.CustomPathfinderImpl;
import ru.collapsedev.collapseapi.util.BukkitUtil;

import java.util.ArrayList;
import java.util.List;

public class CustomEntityService implements Listener {

    public CustomEntityService() {
        Bukkit.getPluginManager().registerEvents(this, APILoader.getInstance());
    }

    private static final List<CustomEntity> customEntities = new ArrayList<>();

    public static void addCustomEntity(CustomEntity customEntity) {
        customEntities.add(customEntity);
    }

    public static void removeCustomEntity(CustomEntity customEntity) {
        customEntities.remove(customEntity);
    }

    @EventHandler
    public void onDisable(PluginDisableEvent event) {
        kill(event.getPlugin());
    }

    public static void kill(Plugin plugin) {
        customEntities.removeIf(customEntity -> {
            if (customEntity.getPlugin() == plugin) {
                kill(customEntity);
                return true;
            }
            return false;
        });
    }

    public static void kill(CustomEntity customEntity) {
        CustomPathfinder pathfinder = CustomPathfinderImpl.parse(
                customEntity.getEntity()
        );
        if (pathfinder != null) {
            pathfinder.remove();
        }
        BukkitUtil.runSync(customEntity::kill);
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (event.getEntity().hasMetadata(CustomEntityImpl.NO_DROP_KEY)) {
            event.getDrops().clear();
        }
    }

}
