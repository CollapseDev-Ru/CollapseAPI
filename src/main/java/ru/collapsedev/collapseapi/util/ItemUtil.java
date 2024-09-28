package ru.collapsedev.collapseapi.util;

import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import lombok.experimental.UtilityClass;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

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
