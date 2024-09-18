package ru.collapsedev.collapseapi.api.entity;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

public interface CustomEntity {

    void setName(String name);
    void setDamage(double damage);
    void setSpeed(double speed);
    void setFollowRange(double followRange);
    void setArmor(double armor);
    double getHeath();
    void setHealth(double health);
    void heal();
    boolean isDeath();
    void kill();
    Location getSpawnLocation();
    Location getCurrentLocation();
    LivingEntity getEntity();
    void setCustomMetadata(Plugin plugin, String keyTag);

}
