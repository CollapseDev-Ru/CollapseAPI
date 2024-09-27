package ru.collapsedev.collapseapi.api.entity.equipments;

import org.bukkit.inventory.ItemStack;
import ru.collapsedev.collapseapi.api.entity.CustomEntity;

public interface EntityEquipments {

    ItemStack getHelmet();
    ItemStack getChestplate();
    ItemStack getLeggings();
    ItemStack getBoots();

    ItemStack getMainHand();
    ItemStack getOffHand();

    void apply(CustomEntity entity);
}
