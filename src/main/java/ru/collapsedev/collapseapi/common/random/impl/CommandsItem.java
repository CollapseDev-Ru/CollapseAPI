package ru.collapsedev.collapseapi.common.random.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import ru.collapsedev.collapseapi.api.random.RandomItem;
import ru.collapsedev.collapseapi.common.object.MapAccessor;
import ru.collapsedev.collapseapi.common.object.Placeholders;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ToString
public class CommandsItem implements RandomItem {

    double chance;
    List<String> commands;

    public static CommandsItem of(MapAccessor accessor) {
        return new CommandsItem(
                accessor.getDouble("chance", 50D),
                accessor.getStringList("commands")
        );
    }

    public void give(String playerName) {
        give(Placeholders.of(Map.of("{player}", playerName)));
    }

    public void give(Placeholders placeholders) {
        commands.forEach(cmd -> Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(), placeholders.apply(cmd)
        ));
    }
}
