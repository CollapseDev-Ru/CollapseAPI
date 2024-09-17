package ru.collapsedev.collapseapi.service;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import ru.collapsedev.collapseapi.APILoader;

import java.util.Map;

public class CommandService implements Listener {

    public CommandService() {
        Bukkit.getPluginManager().registerEvents(this, APILoader.getInstance());
    }

    @EventHandler
    public void onDisablePlugin(PluginDisableEvent event) {
        Plugin plugin = event.getPlugin();

        Map<String, Command> knownCommands = Bukkit.getCommandMap().getKnownCommands();
        knownCommands.values().removeIf(cmd -> hasCustomCommandAndInstanceof(cmd, plugin));
    }


    public boolean hasCustomCommandAndInstanceof(Command command, Plugin plugin) {
        return command instanceof ru.collapsedev.collapseapi.api.command.Command &&
                ((ru.collapsedev.collapseapi.api.command.Command) command).getPlugin() == plugin;
    }

}
