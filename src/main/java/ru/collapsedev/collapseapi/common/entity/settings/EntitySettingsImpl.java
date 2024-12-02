package ru.collapsedev.collapseapi.common.entity.settings;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.EntityType;
import ru.collapsedev.collapseapi.api.entity.CustomEntity;
import ru.collapsedev.collapseapi.api.entity.settings.EntitySettings;
import ru.collapsedev.collapseapi.common.object.MapAccessor;
import ru.collapsedev.collapseapi.util.StringUtil;

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

    public static EntitySettings of(MapAccessor accessor) {
        if (accessor == null) {
            return EntitySettingsImpl.builder()
                    .entityType(EntityType.ZOMBIE)
                    .build();
        }

        return EntitySettingsImpl.builder()
                .entityType(EntityType.valueOf(accessor.getString("entity", "zombie").toUpperCase()))
                .name(StringUtil.color(accessor.getString("name")))
                .health(accessor.getDouble("health", -1D))
                .armor(accessor.getDouble("armor", -1D))
                .damage(accessor.getDouble("damage", -1D))
                .speed(accessor.getDouble("speed", -1D))
                .followRange(accessor.getDouble("follow", -1D))
                .build();
    }
}
