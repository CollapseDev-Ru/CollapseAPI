package ru.collapsedev.collapseapi.util;

import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import lombok.experimental.UtilityClass;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    public ItemStack deserializeItemStack(Map<String, Object> map) {
        return ItemStack.deserialize(deserializeRecursive(map));
    }

    private Map<String, Object> deserializeRecursive(Map<String, Object> map) {
        Map<String, Object> deserializedMap = new HashMap<>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();

            if (value instanceof Map) {
                value = deserializeRecursive(ObjectUtil.castValue(value));
                try {
                    value = ConfigurationSerialization.deserializeObject(ObjectUtil.castValue(value));
                } catch (IllegalArgumentException ignored) {}
            } else if (value instanceof List) {
                List<Object> deserializedList = new ArrayList<>();
                for (Object element : (List<?>) value) {
                    if (element instanceof Map) {
                        Map<String, Object> mapElement = ObjectUtil.castValue(element);
                        if (mapElement.containsKey("==")) {
                            try {
                                deserializedList.add(ConfigurationSerialization.deserializeObject(mapElement));
                            } catch (Exception ignored) {}
                        } else {
                            deserializedList.add(deserializeRecursive(mapElement));
                        }
                    } else {
                        deserializedList.add(element);
                    }
                }
                value = deserializedList;
            }

            deserializedMap.put(entry.getKey(), value);
        }

        return deserializedMap;
    }


}
