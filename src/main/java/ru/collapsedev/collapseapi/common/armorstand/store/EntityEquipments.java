package ru.collapsedev.collapseapi.common.armorstand.store;

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EntityEquipments {
    ItemStack helmet;
    ItemStack body;
    ItemStack leggings;
    ItemStack boots;
    ItemStack mainHand;
    ItemStack offHand;
}