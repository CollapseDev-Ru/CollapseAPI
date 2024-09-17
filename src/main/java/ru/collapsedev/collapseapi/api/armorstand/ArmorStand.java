package ru.collapsedev.collapseapi.api.armorstand;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import ru.collapsedev.collapseapi.common.armorstand.store.EntityEquipments;
import ru.collapsedev.collapseapi.common.armorstand.store.EntitySettings;

public interface ArmorStand {

    void teleport(Location location);
    void setEquipments(EntityEquipments equipments);
    void setSettings(EntitySettings settings);
    void addPlayer(Player player);
    void removePlayer(Player player);
    void hidePlayer(Player player);
    void hideAll();
    Location getLocation();
    void visible(Player player);
    boolean isVisibleAll();
    void setVisibleAll(boolean visible);
}
