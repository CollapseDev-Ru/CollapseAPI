package ru.collapsedev.collapseapi.builder;

import com.cryptomorin.xseries.XMaterial;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.*;


import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.collapsedev.collapseapi.common.object.MapAccessor;
import ru.collapsedev.collapseapi.common.object.Placeholders;
import ru.collapsedev.collapseapi.util.StringUtil;

@Getter
@ToString
@Builder(buildMethodName = "buildFields", setterPrefix = "set")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemBuilder {

    ItemStack itemStack;
    Placeholders placeholders;
    ConfigurationSection section;
    MapAccessor accessor;

    String material;
    XMaterial xMaterial;
    String title;
    List<String> lore;
    List<String> enchants;
    List<String> attributes;
    List<String> potionEffects;
    String potionColor;
    boolean titleIsNullSetEmpty;
    boolean glowing;
    boolean hideEnchants;
    boolean hideAttributes;
    boolean hidePotionEffects;
    boolean hideUnbreakable;
    boolean hideAll;
    boolean unbreakable;
    OfflinePlayer usePlaceholders;

    @Builder.Default
    int amount = -1;

    public ItemStack buildItem() {

        if (section != null) {
            accessor = MapAccessor.of(section);
        }
        if (accessor != null) {
            setFields();
        }

        if (itemStack == null) {
            if (xMaterial != null) {
                this.itemStack = xMaterial.parseItem();
            } else {
                if (material == null) {
                    this.itemStack = new ItemStack(Material.AIR);
                    return itemStack;
                } else if (material.startsWith("player_head=")) {
                    String texture = material.substring(12);
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
            enchants.forEach(enchantArgs -> {
                String[] args = enchantArgs.toUpperCase().split(":");
                NamespacedKey key = NamespacedKey.minecraft(args[0].toLowerCase());
                Enchantment enchantment = Enchantment.getByKey(key);
                if (enchantment == null) {
                    enchantment = Enchantment.getByName(args[1].toUpperCase());
                }

                meta.addEnchant(enchantment, Integer.parseInt(args[1]), true);
            });
        }

        if (potionColor != null) {
            PotionMeta potionMeta = (PotionMeta) meta;
            java.awt.Color hex = java.awt.Color.decode(potionColor);
            potionMeta.setColor(Color.fromRGB(hex.getRed(), hex.getGreen(), hex.getBlue()));
        }

        if (attributes != null) {
            attributes.forEach(attributeArgs -> {
                String[] args = attributeArgs.split(":");

                String attributeName = args[0].replace(".", "_").toUpperCase();
                if (!attributeName.startsWith("GENERIC_")) {
                    attributeName = "GENERIC_" + attributeName;
                }

                double value = Double.parseDouble(args[1].replace("%", ""));
                AttributeModifier.Operation operation = AttributeModifier.Operation.ADD_NUMBER;
                if (args[1].contains("%")) {
                    value /= 100.0D;
                    operation = AttributeModifier.Operation.MULTIPLY_SCALAR_1;
                }

                AttributeModifier modifier = new AttributeModifier(
                        UUID.randomUUID(),
                        attributeName,
                        value,
                        operation
                );
                if (args.length > 2) {
                    modifier = new AttributeModifier(
                            UUID.randomUUID(),
                            attributeName,
                            value,
                            operation,
                            EquipmentSlot.valueOf(args[2].toUpperCase())
                    );
                }

                meta.addAttributeModifier(
                        Attribute.valueOf(attributeName),
                        modifier
                );
            });
        }

        if (potionEffects != null) {
            PotionMeta potionMeta = (PotionMeta) meta;
            potionEffects.forEach(effectArgs -> {
                String[] args = effectArgs.split(":");
                potionMeta.addCustomEffect(
                        new PotionEffect(
                                PotionEffectType.getByName(args[0]), Integer.parseInt(args[2]) * 20,
                                Integer.parseInt(args[1]) - 1
                        ),
                        false
                );
            });
        }

        if (glowing) {
            meta.addEnchant(Enchantment.DAMAGE_ARTHROPODS, 0, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        if (hideAll || hideEnchants) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        if (hideAll || hidePotionEffects) {
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        }

        if (hideAll || hideAttributes) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }

        if (hideAll || hideUnbreakable) {
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        }

        if (unbreakable && !meta.isUnbreakable()) {
            meta.setUnbreakable(true);
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

    private void setFields() {

        if (accessor.containsKey("material") && material == null) {
            this.material = accessor.getString("material");
        }

        if (accessor.containsKey("title") && title == null) {
            this.title = accessor.getString("title");
        }

        if (accessor.containsKey("lore") && lore == null) {
            this.lore = accessor.getStringList("lore");
        }

        if (accessor.containsKey("enchants") && enchants == null) {
            this.enchants = accessor.getStringList("enchants");
        }

        if (accessor.containsKey("potion-effects") && potionEffects == null) {
            this.potionEffects = accessor.getStringList("potion-effects");
        }

        if (accessor.containsKey("attributes") && attributes == null) {
            this.attributes = accessor.getStringList("attributes");
        }

        if (accessor.containsKey("potion-color") && potionColor == null) {
            this.potionColor = accessor.getString("potion-color");
        }

        if (accessor.containsKey("glowing") && !glowing) {
            this.glowing = accessor.getBoolean("glowing");
        }

        if (accessor.containsKey("hide-all") && !hideAll) {
            this.hideAll = accessor.getBoolean("hide-all");
        }

        if (accessor.containsKey("hide-enchants") && !hideEnchants) {
            this.hideEnchants = accessor.getBoolean("hide-enchants");
        }

        if (accessor.containsKey("hide-attributes") && !hideAttributes) {
            this.hideAttributes = accessor.getBoolean("hide-attributes");
        }

        if (accessor.containsKey("hide-potion-effects") && !hidePotionEffects) {
            this.hidePotionEffects = accessor.getBoolean("hide-potion-effects");
        }

        if (accessor.containsKey("hide-unbreakable") && !hideUnbreakable) {
            this.hideUnbreakable = accessor.getBoolean("hide-unbreakable");
        }

        if (accessor.containsKey("unbreakable") && !unbreakable) {
            this.unbreakable = accessor.getBoolean("unbreakable");
        }

        if (accessor.containsKey("amount")) {
            this.amount = accessor.getInt("amount");
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put("material", itemStack.getType().name());

        if (itemStack.getAmount() != 1) {
            map.put("amount", itemStack.getAmount());
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {

            if (meta.hasDisplayName()) {
                map.put("title", StringUtil.legacyToHex(meta.getDisplayName()));
            }

            if (meta.hasLore()) {
                map.put("lore", StringUtil.legacyToHex(meta.getLore()));
            }

            if (meta.hasEnchants()) {
                List<String> enchantsList = new ArrayList<>();
                meta.getEnchants().forEach((enchantment, level) -> {
                    enchantsList.add(enchantment.getKey().getKey() + ":" + level);
                });

                map.put("enchants", enchantsList);
            }

            if (meta instanceof PotionMeta) {
                PotionMeta potionMeta = (PotionMeta) meta;

                List<String> effectsList = new ArrayList<>();
                for (PotionEffect effect : potionMeta.getCustomEffects()) {
                    effectsList.add(effect.getType().getName() + ":"
                            + (effect.getAmplifier() + 1) + ":"
                            + (effect.getDuration() / 20));
                }
                map.put("potion-effects", effectsList);
            }

            if (meta.hasAttributeModifiers()) {
                List<String> attributesList = new ArrayList<>();
                meta.getAttributeModifiers().forEach((attribute, modifier) -> {
                    StringBuilder attrString = new StringBuilder(attribute.name())
                            .append(":")
                            .append(modifier.getAmount());

                    if (modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_SCALAR_1) {
                        attrString.append("%");
                    }

                    if (modifier.getSlot() != null) {
                        attrString.append(":").append(modifier.getSlot().name().toLowerCase());
                    }

                    attributesList.add(attrString.toString());
                });
                map.put("attributes", attributesList);
            }

            if (meta instanceof PotionMeta) {
                PotionMeta potionMeta = (PotionMeta) meta;
                if (potionMeta.hasColor()) {
                    Color color = potionMeta.getColor();
                    map.put("potion-color", String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()));
                }
            }

            if (meta instanceof SkullMeta) {
                SkullMeta skullMeta = (SkullMeta) meta;
                if (skullMeta.hasOwner()) {
                    map.put("material", "player_head=" + skullMeta.getOwner());
                } else {
                    try {
                        Field profileField = skullMeta.getClass().getDeclaredField("profile");
                        profileField.setAccessible(true);
                        GameProfile profile = (GameProfile) profileField.get(skullMeta);
                        if (profile != null && profile.getProperties().containsKey("textures")) {
                            Property property = profile.getProperties().get("textures").iterator().next();
                            map.put("material", property.getValue());
                        }
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

            boolean glowing = meta.hasEnchant(Enchantment.DAMAGE_ARTHROPODS);
            if (glowing) {
                map.put("glowing", true);
            }

            boolean hideEnchants = meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS);
            boolean hideAttributes = meta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES);
            boolean hidePotionEffects = meta.hasItemFlag(ItemFlag.HIDE_POTION_EFFECTS);
            boolean hideUnbreakable = meta.hasItemFlag(ItemFlag.HIDE_UNBREAKABLE);

            boolean hideAll = hideEnchants && hideAttributes && hidePotionEffects && hideUnbreakable;
            if (hideAll) {
                map.put("hide-all", true);
            } else {
                if (hideEnchants) {
                    map.put("hide-enchants", true);
                }
                if (hideAttributes) {
                    map.put("hide-attributes", true);
                }
                if (hidePotionEffects) {
                    map.put("hide-potion-effects", true);
                }
                if (hideUnbreakable) {
                    map.put("hide-unbreakable", true);
                }
            }

            if (meta.isUnbreakable()) {
                map.put("unbreakable", true);
            }
        }

        return map;
    }

}

