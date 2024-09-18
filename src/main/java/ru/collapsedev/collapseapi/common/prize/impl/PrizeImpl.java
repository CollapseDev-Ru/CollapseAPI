package ru.collapsedev.collapseapi.common.prize.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import ru.collapsedev.collapseapi.api.prize.Prize;
import ru.collapsedev.collapseapi.util.RandomUtil;

import java.util.List;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ToString
public class PrizeImpl implements Prize {
    double chance;
    List<String> commands;

    public void give(String playerName) {
        commands.forEach(cmd -> Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(),
                cmd.replace("{player}", playerName)
        ));
    }

    public boolean checkChance() {
        return RandomUtil.randomDouble(100) < chance;
    }

}
