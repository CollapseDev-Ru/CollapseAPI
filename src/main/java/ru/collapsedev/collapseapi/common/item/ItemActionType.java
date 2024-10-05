package ru.collapsedev.collapseapi.common.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.event.Event;


@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemActionType {

    public static final ItemActionType CLICK = new ItemActionType("CLICK");
    public static final ItemActionType ENTITY = new ItemActionType("ENTITY");
    public static final ItemActionType EAT = new ItemActionType("EAT");
    public static final ItemActionType SHIFT = new ItemActionType("SHIFT");
    public static final ItemActionType BREAK = new ItemActionType("BREAK");
    public static final ItemActionType PLACE = new ItemActionType("PLACE");
    public static final ItemActionType DROP = new ItemActionType("DROP");
    public static final ItemActionType SWAP = new ItemActionType("SWAP");
    public static final ItemActionType PICKUP = new ItemActionType("PICKUP");

    String type;

    public <T extends Event> ItemActionWrapper<T> wrap(T event) {
        return new ItemActionWrapper<>(this, event);
    }

    private ItemActionType(String type) {
        this.type = type;
    }

    public static ItemActionType createCustomType(String type) {
        return new ItemActionType(type.toLowerCase());
    }


}
