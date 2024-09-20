package ru.collapsedev.collapseapi.common.prize.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import ru.collapsedev.collapseapi.api.prize.Prize;
import ru.collapsedev.collapseapi.util.ObjectUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ToString
public class CommandsPrize implements Prize {

    double chance;
    List<String> commands;

    public static CommandsPrize ofMap(Map<?, ?> map) {
        double chance = ((Number) map.get("chance")).doubleValue();

        List<String> commands = map.containsKey("command")
                ? Collections.singletonList((String) map.get("command"))
                : ObjectUtil.castValue(map.get("commands"));

        return new CommandsPrize(chance, commands);
    }

    public void give(String playerName) {
        commands.forEach(cmd -> Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(),
                cmd.replace("{player}", playerName)
        ));
    }
}
