package ru.collapsedev.collapseapi;

import lombok.Getter;

import ru.collapsedev.collapseapi.service.MenuService;
import org.bukkit.plugin.java.JavaPlugin;
import ru.collapsedev.collapseapi.service.UpdaterService;

@Getter
public final class APILoader extends JavaPlugin {

    @Getter private static APILoader instance;


    @Override
    public void onEnable() {
        instance = this;

        initServices();
    }



    public void initServices() {
        new UpdaterService(this, "CollapseDev-Ru", "CollapseAPI", "APIUpdater.notify");
        new MenuService();
    }

    @Override
    public void onDisable() {
    }

}
