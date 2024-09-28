package ru.collapsedev.collapseapi.service;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import ru.collapsedev.collapseapi.APILoader;
import ru.collapsedev.collapseapi.api.entity.CustomEntity;
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
        Plugin plugin = event.getPlugin();
        customEntities.removeIf(customEntity -> {
            if (customEntity.getPlugin() == plugin) {
                BukkitUtil.runSync(customEntity::kill);
                return true;
            }
            return false;
        });
    }


}
