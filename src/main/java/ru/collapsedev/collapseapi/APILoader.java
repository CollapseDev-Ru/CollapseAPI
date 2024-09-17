package ru.collapsedev.collapseapi;

import lombok.Getter;

import ru.collapsedev.collapseapi.service.CommandService;
import ru.collapsedev.collapseapi.service.MenuService;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class APILoader extends JavaPlugin {

    @Getter private static APILoader instance;


    @Override
    public void onEnable() {
        instance = this;

        initServices();
    }



    public void initServices() {
//        new UpdaterService(this, "CollapseDev-Ru", "CollapseAPI", "APIUpdater.notify");
        new MenuService();
        new CommandService();
    }

    @Override
    public void onDisable() {
    }

}
