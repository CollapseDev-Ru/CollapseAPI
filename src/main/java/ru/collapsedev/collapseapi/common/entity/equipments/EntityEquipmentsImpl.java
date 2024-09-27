package ru.collapsedev.collapseapi.common.entity.equipments;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import ru.collapsedev.collapseapi.api.entity.CustomEntity;
import ru.collapsedev.collapseapi.api.entity.equipments.EntityEquipments;
import ru.collapsedev.collapseapi.builder.ItemBuilder;
import ru.collapsedev.collapseapi.common.object.MapAccessor;

import java.util.Map;


@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
@Builder
public class EntityEquipmentsImpl implements EntityEquipments {

    ItemStack helmet;
    ItemStack chestplate;
    ItemStack leggings;
    ItemStack boots;

    ItemStack mainHand;
    ItemStack offHand;


    @Override
    public void apply(CustomEntity entity) {
        EntityEquipment equipment = entity.getEntity().getEquipment();

        if (helmet != null) {
            equipment.setHelmet(helmet);
        }
        if (chestplate != null) {
            equipment.setChestplate(chestplate);
        }
        if (leggings != null) {
            equipment.setLeggings(leggings);
        }
        if (boots != null) {
            equipment.setBoots(boots);
        }

        if (mainHand != null) {
            equipment.setItemInMainHand(mainHand);
        }
        if (offHand != null) {
            equipment.setItemInOffHand(offHand);
        }
    }

    public static EntityEquipments ofMap(Map<?, ?> map) {
        MapAccessor accessor = MapAccessor.of(map);

        return EntityEquipmentsImpl.builder()
                .helmet(buildItem(accessor, "helmet"))
                .chestplate(buildItem(accessor, "chestplate"))
                .leggings(buildItem(accessor, "leggings"))
                .boots(buildItem(accessor, "boots"))
                .mainHand(buildItem(accessor, "main-hand"))
                .offHand(buildItem(accessor, "off-hand"))
                .build();
    }

    private static ItemStack buildItem(MapAccessor accessor, String key) {
        if (!accessor.containsKey(key)) {
            return null;
        }
        return ItemBuilder.builder()
                .setMap(accessor.getMap(key))
                .buildFields().buildItem();
    }
}
