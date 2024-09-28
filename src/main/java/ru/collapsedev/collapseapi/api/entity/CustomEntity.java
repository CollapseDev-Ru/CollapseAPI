package ru.collapsedev.collapseapi.api.entity;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import ru.collapsedev.collapseapi.api.entity.equipments.EntityEquipments;
import ru.collapsedev.collapseapi.api.entity.settings.EntitySettings;
import ru.collapsedev.collapseapi.api.pathfinder.CustomPathfinder;

public interface CustomEntity {

    Plugin getPlugin();
    LivingEntity getEntity();

    Location getSpawnLocation();
    Location getCurrentLocation();
    double getHeath();

    void setCustomMetadata(Plugin plugin, String keyTag);
    void setCustomMetadata(String keyTag, MetadataValue metadata);
    void setName(String name);
    void setDamage(double damage);
    void setSpeed(double speed);
    void setFollowRange(double followRange);
    void setArmor(double armor);
    void setHealth(double health);
    void setEquipments(EntityEquipments equipments);
    void setSettings(EntitySettings settings);

    void heal();
    boolean isDeath();
    void kill();

    CustomPathfinder moveTo(Location location, double speed, int startDelay);
    CustomPathfinder moveTo(Location location, double speed);


}
