package ru.collapsedev.collapseapi;

import lombok.Getter;

import org.bukkit.Bukkit;
import ru.collapsedev.collapseapi.common.menu.MenuImpl;
import ru.collapsedev.collapseapi.service.*;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class APILoader extends JavaPlugin {

    @Getter
    private static APILoader instance;

    MenuService menuService;

    @Override
    public void onEnable() {
        instance = this;

        initServices();
    }


    public void initServices() {
//        new UpdaterService(this, "CollapseDev-Ru", "CollapseAPI", "APIUpdater.notify");
        menuService = new MenuService();
        new CommandService();
        new CustomEntityService();
        new CustomPathfinderService();
        new CustomItemsListener();
        new ArmorStandsService();
    }

    @Override
    public void onDisable() {
        CustomEntityService.killAll();
        Bukkit.getOnlinePlayers().forEach(player -> {
            MenuImpl menu = menuService.getMenu(player.getOpenInventory().getTopInventory());
            if (menu != null) {
                player.closeInventory();
            }
        });
    }

}
