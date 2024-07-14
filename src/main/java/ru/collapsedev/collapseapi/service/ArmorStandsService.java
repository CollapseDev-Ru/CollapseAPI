package ru.collapsedev.collapseapi.service;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import ru.collapsedev.collapseapi.APILoader;
import ru.collapsedev.collapseapi.api.armorstand.ArmorStand;

import java.util.*;

public class ArmorStandsService implements Listener {
    private static final Map<Plugin, List<ArmorStand>> pluginArmorStands = new HashMap<>();

    public static void addArmorStand(Plugin plugin, ArmorStand armorStand) {
        pluginArmorStands.computeIfAbsent(
                plugin,
                s -> new ArrayList<>()
        ).add(armorStand);
    }

    public static void addArmorStands(Plugin plugin, List<ArmorStand> armorStands) {
        armorStands.forEach(armorStand -> addArmorStand(plugin, armorStand));
    }

    public static void removeArmorStand(Plugin plugin, ArmorStand stand) {
        getPluginArmorStands(plugin).remove(stand);
    }

    public static List<ArmorStand> getPluginArmorStands(Plugin plugin) {
        return pluginArmorStands.getOrDefault(
                plugin,
                Collections.emptyList()
        );
    }

    public static void killAll(Plugin plugin) {
        List<ArmorStand> armorStands = getPluginArmorStands(plugin);
        armorStands.forEach(ArmorStand::hideAll);
        armorStands.clear();
    }

    public ArmorStandsService() {
        Bukkit.getPluginManager().registerEvents(this, APILoader.getInstance());
    }

    @EventHandler
    public void onChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        pluginArmorStands.values().forEach(armorStands
                -> armorStands.forEach(armorStand
                -> armorStand.visible(player))
        );
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        pluginArmorStands.values().forEach(armorStands
                -> armorStands.forEach(armorStand
                -> armorStand.hidePlayer(player))
        );
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        pluginArmorStands.values().forEach(armorStands
                -> armorStands.forEach(armorStand -> {
                    if (armorStand.isVisibleAll()) {
                        armorStand.addPlayer(player);
                    }
                }
        ));
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        killAll(event.getPlugin());
    }
}
