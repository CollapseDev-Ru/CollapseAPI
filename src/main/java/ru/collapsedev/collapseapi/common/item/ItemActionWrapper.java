package ru.collapsedev.collapseapi.common.item;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.event.Event;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemActionWrapper<T extends Event> {
    ItemActionType actionType;
    T event;

    public static <T extends Event> ItemActionWrapper<T> of(ItemActionType actionType, T event) {
        return new ItemActionWrapper<>(actionType, event);
    }
}