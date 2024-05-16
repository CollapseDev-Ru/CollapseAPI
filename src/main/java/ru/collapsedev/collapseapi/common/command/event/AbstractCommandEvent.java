package ru.collapsedev.collapseapi.common.command.event;


import lombok.*;
import ru.collapsedev.collapseapi.api.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@RequiredArgsConstructor
public abstract class AbstractCommandEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Command command;

    public HandlerList getHandlers() {
        return handlers;
    }

}