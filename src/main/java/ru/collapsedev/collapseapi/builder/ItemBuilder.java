package ru.collapsedev.collapseapi.builder;

import com.cryptomorin.xseries.XMaterial;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.*;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.*;


import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import ru.collapsedev.collapseapi.common.object.MapAccessor;
import ru.collapsedev.collapseapi.common.object.Placeholders;
import ru.collapsedev.collapseapi.util.StringUtil;

@Getter
@ToString
@Builder(buildMethodName = "buildFields", setterPrefix = "set")
public class ItemBuilder {

    private ItemStack itemStack;
    private Placeholders placeholders;
    private ConfigurationSection section;
    private Map<?, ?> map;

    private String material;
    private XMaterial xMaterial;
    private String title;
    private List<String> lore;
    private List<String> enchants;
    private String potionColor;
    private boolean titleIsNullSetEmpty;
    private boolean glowing;
    private boolean hideEnchants;
    private boolean hideAttributes;
    private boolean hidePotionEffects;
    private OfflinePlayer usePlaceholders;

    @Builder.Default
    private int amount = -1;

    public ItemStack buildItem() {

        if (section != null) {
            setFieldsFromSection();
        }
        if (map != null) {
            setFieldsFromMap();
        }

        if (itemStack == null) {
            if (xMaterial != null) {
                this.itemStack = xMaterial.parseItem();
            } else {
                if (material == null) {
                    this.itemStack = new ItemStack(Material.AIR);
                    return itemStack;
                } else if (material.startsWith("player_head=")) {
                    String texture = material.substring(8);
                    this.itemStack = setSkull(texture, true);
                } else if (material.length() > 128) {
                    this.itemStack = setSkull(material, false);
                } else {
                    Optional<XMaterial> optionalXMaterial = XMaterial.matchXMaterial(material);
                    this.itemStack = optionalXMaterial.orElseThrow().parseItem();
                }
            }
        }

        if (itemStack == null) {
            throw new RuntimeException("ItemStack is null");
        }

        if (amount != -1) {
            itemStack.setAmount(amount);
        }

        ItemMeta meta = itemStack.getItemMeta();

        if (title != null) {
            if (placeholders != null) {
                this.title = placeholders.apply(title);
            }
            if (usePlaceholders != null) {
                this.title = StringUtil.placeholdersColor(usePlaceholders, title);
            } else {
                this.title = StringUtil.color(title);
            }
            meta.setDisplayName(title);
        } else if (titleIsNullSetEmpty) {
            meta.setDisplayName(" ");
        }

        if (lore != null) {
            if (placeholders != null) {
                this.lore = placeholders.apply(lore);
            }
            if (usePlaceholders != null) {
                this.lore = StringUtil.placeholdersColor(usePlaceholders, lore);
            } else {
                this.lore = StringUtil.color(lore);
            }
            meta.setLore(lore);
        }

        if (enchants != null) {
            enchants.forEach(enchant -> {
                String[] args = enchant.toUpperCase().split(":");
                if (args[0].equals("PROTECTION")) {
                    args[0] = "PROTECTION_ENVIRONMENTAL";
                }
                meta.addEnchant(Enchantment.getByName(args[0]), Integer.parseInt(args[1]), true);
            });
        }

        if (potionColor != null) {
            PotionMeta potionMeta = (PotionMeta) meta;
            java.awt.Color hex = java.awt.Color.decode(potionColor);
            potionMeta.setColor(Color.fromRGB(hex.getRed(), hex.getGreen(), hex.getBlue()));
        }

        if (glowing) {
            meta.addEnchant(Enchantment.MENDING, 0, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        if (hideEnchants) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        if (hidePotionEffects) {
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        }

        if (hideAttributes) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @SneakyThrows
    private ItemStack setSkull(String texture, boolean playerHead) {
        ItemStack itemStack = XMaterial.PLAYER_HEAD.parseItem();

        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();

        if (!playerHead) {
            GameProfile profile = new GameProfile(UUID.randomUUID(), "");
            profile.getProperties().put("textures", new Property("textures", texture));

            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } else {
            meta.setOwner(texture);
        }

        itemStack.setItemMeta(meta);

        return itemStack;
    }

    private void setFieldsFromSection() {
        section.getKeys(false).forEach(key -> {
            switch (key.toLowerCase()) {
                case "material": {
                    if (material == null) {
                        this.material = section.getString("material");
                    }
                    return;
                }
                case "title": {
                    if (title == null) {
                        this.title = section.getString("title");
                    }
                    return;
                }
                case "lore": {
                    if (lore == null) {
                        this.lore = section.getStringList("lore");
                    }
                    return;
                }
                case "enchants": {
                    if (enchants == null) {
                        this.enchants = section.getStringList("enchants");
                    }
                    return;
                }
                case "potion_color": {
                    if (potionColor == null) {
                        this.potionColor = section.getString("potion_color");
                    }
                    return;
                }
                case "glowing": {
                    if (!glowing) {
                        this.glowing = section.getBoolean("glowing");
                    }
                    return;
                }
                case "hide_enchants": {
                    if (!hideEnchants) {
                        this.hideEnchants = section.getBoolean("hide_enchants");
                    }
                    return;
                }
                case "hide_attributes": {
                    if (!hideAttributes) {
                        this.hideAttributes = section.getBoolean("hide_attributes");
                    }
                    return;
                }
                case "hide_potion_effects": {
                    if (!hidePotionEffects) {
                        this.hidePotionEffects = section.getBoolean("hide_potion_effects");
                    }
                    return;
                }
                case "amount": {
                    if (amount == 1 || amount == 0) {
                        this.amount = section.getInt("amount");
                    }
                    return;
                }
            }
        });
    }

    private void setFieldsFromMap() {
        MapAccessor accessor = MapAccessor.of(map);

        if (accessor.containsKey("material")) {
            if (material == null) {
                this.material = accessor.getString("material");
            }
        }

        if (accessor.containsKey("title")) {
            if (title == null) {
                this.title = accessor.getString("title");
            }
        }

        if (accessor.containsKey("lore")) {
            if (lore == null) {
                this.lore = accessor.getStringList("lore");
            }
        }

        if (accessor.containsKey("enchants")) {
            if (enchants == null) {
                this.enchants = accessor.getStringList("enchants");
            }
        }

        if (accessor.containsKey("potion_color")) {
            if (potionColor == null) {
                this.potionColor = accessor.getString("potion_color");
            }
        }

        if (accessor.containsKey("glowing")) {
            if (!glowing) {
                this.glowing = accessor.getBoolean("glowing");
            }
        }

        if (accessor.containsKey("hide_enchants")) {
            if (!hideEnchants) {
                this.hideEnchants = accessor.getBoolean("hide_enchants");
            }
        }

        if (accessor.containsKey("hide_attributes")) {
            if (!hideAttributes) {
                this.hideAttributes = accessor.getBoolean("hide_attributes");
            }
        }

        if (accessor.containsKey("hide_potion_effects")) {
            if (!hidePotionEffects) {
                this.hidePotionEffects = accessor.getBoolean("hide_potion_effects");
            }
        }

        if (accessor.containsKey("amount")) {
            if (amount == 1 || amount == 0) {
                this.amount = accessor.getInt("amount");
            }
        }
    }
}

