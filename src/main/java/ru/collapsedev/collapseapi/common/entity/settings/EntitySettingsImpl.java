package ru.collapsedev.collapseapi.common.entity.settings;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import ru.collapsedev.collapseapi.api.entity.CustomEntity;
import ru.collapsedev.collapseapi.api.entity.settings.EntitySettings;
import ru.collapsedev.collapseapi.common.object.MapAccessor;
import ru.collapsedev.collapseapi.util.StringUtil;

import java.util.Map;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
@Builder
public class EntitySettingsImpl implements EntitySettings {
    EntityType entityType;
    String name;
    double health;
    double armor;
    double damage;
    double speed;
    double followRange;

    public void apply(CustomEntity entity) {
        if (name != null) {
            entity.setName(name);
        }
        if (health > 0) {
            entity.setHealth(health);
        }
        if (armor > 0) {
            entity.setArmor(armor);
        }
        if (damage > 0) {
            entity.setDamage(damage);
        }
        if (speed > 0) {
            entity.setSpeed(speed);
        }
        if (followRange > 0) {
            entity.setFollowRange(followRange);
        }
    }

    public static EntitySettings ofMap(Map<?, ?> map) {
        MapAccessor accessor = MapAccessor.of(map);

        return EntitySettingsImpl.builder()
                .entityType(EntityType.valueOf(accessor.getString("entity").toUpperCase()))
                .name(StringUtil.color(accessor.getString("name")))
                .health(accessor.getDouble("health"))
                .armor(accessor.getDouble("armor"))
                .damage(accessor.getDouble("damage"))
                .speed(accessor.getDouble("speed"))
                .followRange(accessor.getDouble("follow"))
                .build();
    }
}
