package ru.collapsedev.collapseapi.api.entity.settings;

import org.bukkit.entity.EntityType;
import ru.collapsedev.collapseapi.api.entity.CustomEntity;

public interface EntitySettings {

    EntityType getEntityType();
    String getName();
    double getHealth();
    double getArmor();
    double getDamage();
    double getSpeed();
    double getFollowRange();

    void apply(CustomEntity entity);

}
