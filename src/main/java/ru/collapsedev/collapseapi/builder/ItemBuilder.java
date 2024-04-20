package ru.collapsedev.collapseapi.builder;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import jdk.jfr.Description;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import ru.collapsedev.collapseapi.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


@NoArgsConstructor
public class ItemBuilder extends ItemStack {
    private Map<String, String> placeholders;

    private ConfigurationSection section;

    public ItemBuilder(ConfigurationSection section) {
        this.section = section;
    }

    public ItemBuilder setPlaceholders(Map<String, String> placeholders) {
        this.placeholders = placeholders;
        return this;
    }

    @SneakyThrows
    public void skull(String str) {
        super.setType(Material.valueOf("SKULL_ITEM"));
        super.setDurability((short) 3);

        SkullMeta meta = (SkullMeta) this.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", str));

        Field profileField = meta.getClass().getDeclaredField("profile");
        profileField.setAccessible(true);
        profileField.set(meta, profile);

        super.setItemMeta(meta);
    }


    public void setMaterial(String[] material) {
        if (material[0].length() > 30) {
            skull(material[0]);
        } else {
            if (StringUtils.isNumeric(material[0])) {
//                super.setType(Material.getMaterial(Integer.parseInt(material[0])));
            } else {
                super.setType(Material.valueOf(material[0].toUpperCase()));
            }
        }

        if (material.length == 2) {
            super.setDurability(Short.parseShort(material[1]));
        }
    }

    public void setTitle(String title) {
        ItemMeta meta = super.getItemMeta();

        AtomicReference<String> atomicTitle = new AtomicReference<>(title);
        if (placeholders != null) {
            placeholders.forEach((placeholder, value) -> {
                atomicTitle.set(atomicTitle.get().replace(placeholder, value));
            });
        }

        meta.setDisplayName(StringUtil.color(atomicTitle.get()));
        super.setItemMeta(meta);

    }

    @Description("Cringe code")
    public void setLore(List<String> lore) {
        ItemMeta meta = super.getItemMeta();

        AtomicReference<List<String>> atomicLore = new AtomicReference<>(lore);
        if (placeholders != null) {
            placeholders.forEach((placeholder, value) -> {
                if (value.contains("\n")) {
                    List<String> newLore = new ArrayList<>();
                    for (String line : lore) {
                        if (line.contains(placeholder)) {
                            newLore.addAll(Arrays.asList(value.split("\n")));
                        } else {
                            newLore.add(line);
                        }
                    }
                    atomicLore.set(newLore);
                } else {
                    atomicLore.get().replaceAll(line -> line.replace(placeholder, value));
                }
            });
        }

        meta.setLore(StringUtil.color(atomicLore.get()));
        super.setItemMeta(meta);
    }

    public void setEnchants(List<String> enchants) {
        ItemMeta meta = super.getItemMeta();
        enchants.forEach(enchant -> {
            String[] args = enchant.toUpperCase().split(":");
            meta.addEnchant(Enchantment.getByName(args[0]), Integer.parseInt(args[1]), true);
        });
        super.setItemMeta(meta);
    }

    public void setPotionColor(String color) {
        ItemMeta meta = super.getItemMeta();
        PotionMeta potionMeta = (PotionMeta) meta;
        java.awt.Color hex = java.awt.Color.decode(color);
        potionMeta.setColor(Color.fromRGB(hex.getRed(), hex.getGreen(), hex.getBlue()));
        super.setItemMeta(meta);
    }

    public void setGlowing() {
        ItemMeta meta = super.getItemMeta();
        meta.addEnchant(Enchantment.MENDING, 0, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        super.setItemMeta(meta);
    }

    public void setHideEnchants() {
        ItemMeta meta = getItemMeta();
        super.getItemMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
        super.setItemMeta(meta);
    }

    public void setHideAttributes() {
        ItemMeta meta = getItemMeta();
        super.getItemMeta().addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        super.setItemMeta(meta);
    }

    public void setHidePotionEffects() {
        ItemMeta meta = getItemMeta();
        super.getItemMeta().addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        super.setItemMeta(meta);
    }

    public ItemStack build() {
        if (getType() == Material.AIR || getType() == null) {
            super.setType(Material.AIR);
        }

        section.getKeys(false).forEach(key -> {
            switch (key.toLowerCase()) {
                case "material": {
                    setMaterial(section.getString(key).split(":"));
                    return;
                }
                case "title": {
                    setTitle(section.getString(key));
                    return;
                }
                case "lore": {
                    setLore(section.getStringList(key));
                    return;
                }
                case "enchants": {
                    setEnchants(section.getStringList(key));
                    return;
                }
                case "potion_color": {
                    setPotionColor(section.getString(key));
                    return;
                }
                case "glowing": {
                    if (!section.getBoolean(key))
                        return;
                    setGlowing();
                    return;
                }
                case "hide_enchants": {
                    if (!section.getBoolean(key))
                        return;
                    setHideEnchants();
                    return;
                }
                case "hide_attributes": {
                    if (!section.getBoolean(key))
                        return;
                    setHideAttributes();
                    return;
                }
                case "hide_potion_effects": {
                    if (!section.getBoolean(key))
                        return;
                    setHidePotionEffects();
                    return;
                }
                case "amount": {
                    setAmount(section.getInt(key));
                    return;
                }
            }
        });


        if (super.getAmount() == 0)
            super.setAmount(1);

        return this;

    }
}
