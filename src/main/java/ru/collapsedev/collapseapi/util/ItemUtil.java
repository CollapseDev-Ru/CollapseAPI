package ru.collapsedev.collapseapi.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

@UtilityClass
public class ItemUtil {

    public MaterialData parseMaterialData(ItemStack itemStack) {
        return new MaterialData(itemStack.getType(), itemStack.getData().getData());
    }

    public MaterialData parseMaterialData(Block block) {
        return new MaterialData(block.getType(), block.getData());
    }

    public MaterialData parseMaterialData(String[] item) {
        Material material = parseMaterial(item);
        byte data = item.length == 1 ? 0 : Byte.parseByte(item[1]);

        return new MaterialData(material, data);
    }

    public Material parseMaterial(String name) {
        return name != null ? parseMaterial(name.split(":")) : Material.AIR;
    }
    public Material parseMaterial(String[] item) {
        return Material.getMaterial(item[0].toUpperCase());
//        StringUtils.isNumeric(item[0])
//                ? Material.getMaterial(Integer.parseInt(item[0]))
//                : ;
    }
}
