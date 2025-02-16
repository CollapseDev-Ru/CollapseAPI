package ru.collapsedev.collapseapi.common.entity;

import com.cryptomorin.xseries.XMaterial;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import ru.collapsedev.collapseapi.api.entity.CustomEntity;
import ru.collapsedev.collapseapi.api.entity.equipments.EntityEquipments;
import ru.collapsedev.collapseapi.api.entity.settings.EntitySettings;
import ru.collapsedev.collapseapi.api.pathfinder.CustomPathfinder;
import ru.collapsedev.collapseapi.common.entity.equipments.EntityEquipmentsImpl;
import ru.collapsedev.collapseapi.common.entity.settings.EntitySettingsImpl;
import ru.collapsedev.collapseapi.common.filling.Filling;
import ru.collapsedev.collapseapi.common.filling.type.Layers;
import ru.collapsedev.collapseapi.common.object.MapAccessor;
import ru.collapsedev.collapseapi.common.object.Points;
import ru.collapsedev.collapseapi.common.pathfinder.CustomPathfinderImpl;
import ru.collapsedev.collapseapi.service.CustomEntityService;

import java.util.UUID;
import java.util.logging.Level;


@Log
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomEntityImpl implements CustomEntity {

    public static final String NO_DROP_KEY = "no-drop";

    @Getter
    final Plugin plugin;
    final Location location;
    final EntityType entityType;
    final UUID entityUuid;

    @Getter
    Entity entity;

    public CustomEntityImpl(Plugin plugin, Location location, EntitySettings settings) {
        this.plugin = plugin;
        this.location = location;
        this.entityType = settings.getEntityType();

        spawn();

        CustomEntityService.addCustomEntity(this);

        setSettings(settings);
        this.entityUuid = entity.getUniqueId();
    }

    public static CustomEntity of(Plugin plugin, Location location, MapAccessor accessor) {
        EntitySettings entitySettings = EntitySettingsImpl.of(accessor.getAccessor("settings"));
        EntityEquipments entityEquipments = EntityEquipmentsImpl.of(accessor.getAccessor("equipments"));

        CustomEntityImpl customEntity = new CustomEntityImpl(plugin, location, entitySettings);
        customEntity.setEquipments(entityEquipments);

        return customEntity;
    }

    private void spawn() {
        this.entity = location.getWorld().spawnEntity(location, entityType);
        if (entity instanceof LivingEntity) {
            ((LivingEntity) entity).setRemoveWhenFarAway(false);
        }
    }

    @Override
    public void setCustomMetadata(Plugin plugin, String keyTag) {
        setCustomMetadata(keyTag, new FixedMetadataValue(plugin, entity.getUniqueId()));
    }

    @Override
    public void setCustomMetadata(String keyTag, MetadataValue metadata) {
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
        return getAttribute(Attribute.GENERIC_MAX_HEALTH);
    }

    private AttributeInstance getArmorAttribute() {
        return getAttribute(Attribute.GENERIC_ARMOR);
    }

    private AttributeInstance getDamageAttribute() {
        return getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
    }

    private AttributeInstance getSpeedAttribute() {
        return getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
    }

    private AttributeInstance getFollowRangeAttribute() {
        return getAttribute(Attribute.GENERIC_FOLLOW_RANGE);
    }
    
    private AttributeInstance getAttribute(Attribute attribute) {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity) entity).getAttribute(attribute);
        }
        return null;
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
        if (entity instanceof LivingEntity) {
            return ((LivingEntity) entity).getHealth();
        }
        return 0;
    }

    @Override
    public void setHealth(double health) {
        if (entity instanceof LivingEntity) {
            getHealthMaxAttribute().setBaseValue(health);
            ((LivingEntity) entity).setHealth(health);
        }
    }

    @Override
    public void heal() {
        if (entity instanceof LivingEntity) {
            double maxHealth = getHealthMaxAttribute().getValue();
            ((LivingEntity) entity).setHealth(maxHealth);
        }

    }

    @Override
    public boolean isDeath() {
        return this.entity.isDead();
    }

    @Override
    public void kill() {
        Chunk chunk = entity.getLocation().getChunk();
        chunk.load();
        for (Entity chunkEntity : chunk.getEntities()) {
            if (chunkEntity.getUniqueId().equals(entityUuid)) {
                chunkEntity.remove();
            }
        }
        CustomEntityService.removeCustomEntity(this);
    }

    @Override
    public Location getSpawnLocation() {
        return this.location;
    }

    @Override
    public Location getCurrentLocation() {
        return this.entity.getLocation();
    }

    @Override
    public CustomPathfinder moveTo(Location location, double speed, int startDelay) {
        CustomPathfinder customPathfinder = CustomPathfinderImpl.create(
                plugin, entity, location, speed
        );
        customPathfinder.move(startDelay);
        return customPathfinder;
    }

    @Override
    public CustomPathfinder moveTo(Location location, double speed) {
        return moveTo(location, speed, 20);
    }

    public void setNoDrop(boolean noDrop) {
        if (noDrop) {
            setCustomMetadata(plugin, NO_DROP_KEY);
        } else {
            entity.removeMetadata(NO_DROP_KEY, plugin);
        }
    }

    public boolean isNoDrop() {
        return entity.hasMetadata(NO_DROP_KEY);
    }
}
