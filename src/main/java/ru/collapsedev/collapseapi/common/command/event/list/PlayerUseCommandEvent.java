package ru.collapsedev.collapseapi.common.command.event.list;

import lombok.Getter;
import ru.collapsedev.collapseapi.api.command.Command;
import ru.collapsedev.collapseapi.common.command.event.AbstractCommandEvent;
import org.bukkit.entity.Player;

@Getter
public class PlayerUseCommandEvent extends AbstractCommandEvent {
    private final boolean success;
    public PlayerUseCommandEvent(Player player, Command command, boolean success) {
        super(player, command);
        this.success = success;
    }
}
