package ru.collapsedev.collapseapi.api.item;

import ru.collapsedev.collapseapi.common.item.ItemActionType;

import java.util.Set;

public interface ClickableItem {

    Set<ItemActionType> getActionTypes();

    boolean isClickable();

}
