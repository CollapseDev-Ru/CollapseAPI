package ru.collapsedev.collapseapi.common.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import ru.collapsedev.collapseapi.api.entity.CustomEntity;
import ru.collapsedev.collapseapi.api.entity.equipments.EntityEquipments;
import ru.collapsedev.collapseapi.api.entity.settings.EntitySettings;
import ru.collapsedev.collapseapi.common.entity.equipments.EntityEquipmentsImpl;
import ru.collapsedev.collapseapi.common.entity.settings.EntitySettingsImpl;
import ru.collapsedev.collapseapi.common.object.MapAccessor;

import java.util.Map;
import java.util.logging.Level;


@Log
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomEntityImpl implements CustomEntity {

    final Location location;
    final EntityType entityType;

    @Getter
    LivingEntity entity;

    public CustomEntityImpl(Location location, EntitySettings settings) {
        this.location = location;
        this.entityType = settings.getEntityType();

        spawn();

        setSettings(settings);
    }

    public static CustomEntity of(Location location, Map<?, ?> map) {
        MapAccessor accessor = MapAccessor.of(map);
        EntitySettings entitySettings = EntitySettingsImpl.ofMap(accessor.getMap("settings"));
        EntityEquipments entityEquipments = EntityEquipmentsImpl.ofMap(accessor.getMap("equipments"));

        CustomEntityImpl customEntity = new CustomEntityImpl(location, entitySettings);
        customEntity.setEquipments(entityEquipments);

        return customEntity;
    }

    private void spawn() {
        this.entity = (LivingEntity) location.getWorld().spawnEntity(location, entityType);
        entity.setRemoveWhenFarAway(false);
    }

    @Override
    public void setCustomMetadata(Plugin plugin, String keyTag) {
        FixedMetadataValue metadata = new FixedMetadataValue(plugin, entity.getUniqueId());
        this.entity.setMetadata(keyTag, metadata);
    }

    @Override
    public void setEquipments(EntityEquipments equipments) {
        equipments.apply(this);
    }

    @Override
    public void setSettings(EntitySettings settings) {
        settings.apply(this);
    }

    @Override
    public void setName(String name) {
        this.entity.setCustomName(name);
        this.entity.setCustomNameVisible(true);
    }

    private AttributeInstance getHealthMaxAttribute() {
        return this.entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
    }

    private AttributeInstance getArmorAttribute() {
        return this.entity.getAttribute(Attribute.GENERIC_ARMOR);
    }

    private AttributeInstance getDamageAttribute() {
        return this.entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
    }

    private AttributeInstance getSpeedAttribute() {
        return this.entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
    }

    private AttributeInstance getFollowRangeAttribute() {
        return this.entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE);
    }

    @Override
    public void setDamage(double damage) {
        AttributeInstance damageAttribute = getDamageAttribute();
        if (damageAttribute == null) {
            log.log(Level.SEVERE, "'damage' attribute is not supported for type '{}'", entityType);
            return;
        }
        damageAttribute.setBaseValue(damage);
    }

    @Override
    public void setSpeed(double speed) {
        AttributeInstance speedAttribute = getSpeedAttribute();
        if (speedAttribute == null) {
            log.log(Level.SEVERE, "'speed' attribute is not supported for type '{}'", entityType);
            return;
        }
        speedAttribute.setBaseValue(speed);
    }

    @Override
    public void setFollowRange(double followRange) {
        AttributeInstance followRangeAttribute = getFollowRangeAttribute();
        if (followRangeAttribute == null) {
            log.log(Level.SEVERE, "'followRange' attribute is not supported for type '{}'", entityType);
            return;
        }
        followRangeAttribute.setBaseValue(followRange);
    }

    @Override
    public void setArmor(double armor) {
        AttributeInstance armorAttribute = getArmorAttribute();
        if (armorAttribute == null) {
            log.log(Level.SEVERE, "'armor' attribute is not supported for type '{}'", entityType);
            return;
        }
        armorAttribute.setBaseValue(armor);
    }

    @Override
    public double getHeath() {
        return this.entity.getHealth();
    }

    @Override
    public void setHealth(double health) {
        getHealthMaxAttribute().setBaseValue(health);
        this.entity.setHealth(health);
    }

    @Override
    public void heal() {
        double maxHealth = getHealthMaxAttribute().getValue();
        this.entity.setHealth(maxHealth);
    }

    @Override
    public boolean isDeath() {
        return this.entity.isDead();
    }

    @Override
    public void kill() {
        this.entity.remove();
    }

    @Override
    public Location getSpawnLocation() {
        return this.location;
    }

    @Override
    public Location getCurrentLocation() {
        return this.entity.getLocation();
    }
}
