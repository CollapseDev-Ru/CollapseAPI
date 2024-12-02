package ru.collapsedev.collapseapi.common.object;

import ru.collapsedev.collapseapi.api.menu.Menu;
import ru.collapsedev.collapseapi.api.menu.action.MenuAction;

import java.util.Map;

public class DefaultMenuActions {

    private static final Map<String, MenuAction> actions = DefaultActions.getMenuActions();

    public static void apply(Menu menu) {
        actions.forEach(menu::addAction);
    }
}
