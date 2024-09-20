package ru.collapsedev.collapseapi.common.object;

import org.bukkit.entity.Player;

import java.util.List;

@Deprecated
public class Actions {
    @Deprecated
    public static void use(List<String> actions, Player player) {
        DefaultActions.execute(player, actions);
    }
}
