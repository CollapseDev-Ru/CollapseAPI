package ru.collapsedev.collapseapi;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import lombok.Getter;
import org.bukkit.Bukkit;
import ru.collapsedev.collapseapi.common.menu.MenuImpl;
import ru.collapsedev.collapseapi.service.MenuService;
import org.bukkit.plugin.java.JavaPlugin;
import ru.collapsedev.collapseapi.service.UpdaterService;

@Getter
public final class APILoader extends JavaPlugin {

    @Getter private static APILoader instance;

    @Getter private static PacketEventsAPI<?> packetEventsAPI;


    @Override
    public void onEnable() {
        instance = this;

        initPlugins();
        initServices();
    }

    private void initPlugins() {
        packetEventsAPI = PacketEvents.getAPI();
    }

    public void initServices() {
        new UpdaterService(this, "CollapseDev-Ru", "CollapseAPI", "APIUpdater.notify");
        new MenuService();
    }

    @Override
    public void onDisable() {
    }

}
