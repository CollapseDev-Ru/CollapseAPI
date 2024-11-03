package ru.collapsedev.collapseapi;

import lombok.Getter;

import ru.collapsedev.collapseapi.service.*;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class APILoader extends JavaPlugin {

    @Getter
    private static APILoader instance;


    @Override
    public void onEnable() {
        instance = this;

        initServices();
    }


    public void initServices() {
//        new UpdaterService(this, "CollapseDev-Ru", "CollapseAPI", "APIUpdater.notify");
        new MenuService();
        new CommandService();
        new CustomEntityService();
        new CustomPathfinderService();
        new CustomItemsListener();
    }

    @Override
    public void onDisable() {
        CustomEntityService.killAll();
    }

}
