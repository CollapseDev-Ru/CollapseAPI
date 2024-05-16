package ru.collapsedev.collapseapi.util;

import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

@UtilityClass
public class ItemUtil {

    public XMaterial parseMaterial(ItemStack itemStack) {
        return XMaterial.matchXMaterial(itemStack);
    }

    public XMaterial parseMaterial(Block block) {
        return XBlock.getType(block);
    }

    public XMaterial parseMaterial(String material) {
        return XMaterial.matchXMaterial(material).orElse(XMaterial.AIR);
    }
}
