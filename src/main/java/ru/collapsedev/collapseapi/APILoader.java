package ru.collapsedev.collapseapi;

import lombok.Getter;
import org.bukkit.Bukkit;
import ru.collapsedev.collapseapi.common.modules.PluginModules;
import ru.collapsedev.collapseapi.service.MenuService;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class APILoader extends JavaPlugin {

    @Getter
    private static APILoader instance;

    private PluginModules pluginModules;

    private boolean enabledPlaceholderAPI;

    @Override
    public void onEnable() {
        instance = this;

        initModules();
        initServices();
    }

    public void initModules() {
        this.pluginModules = new PluginModules();
        this.enabledPlaceholderAPI = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    public void initServices() {
        //new UpdaterService(this, "Underly0", "UnderlyAPI");
        new MenuService();
    }

    @Override
    public void onDisable() {
        pluginModules.unloadModules();
    }

}
