package ru.collapsedev.collapseapi.api.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.collapsedev.collapseapi.common.item.ItemActionWrapper;

public interface ItemAction {

    void useAction(ItemActionWrapper<?> wrapper, Player player, ItemStack itemStack);

}
