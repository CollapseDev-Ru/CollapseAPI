package ru.collapsedev.collapseapi.common.hologramtop;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import ru.collapsedev.collapseapi.util.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

public class HologramTop {

    public HologramTop(Player player, String placeholder) {

        Map<OfflinePlayer, Double> topPlayers = Arrays.stream(Bukkit.getOfflinePlayers())
                .collect(Collectors.toMap(
                        offlinePlayer -> offlinePlayer,
                        offlinePlayer -> {

                            String parse = StringUtil.applyPlaceholders(offlinePlayer, placeholder);
                            return parse.isEmpty() ? 0 : NumberUtils.createNumber(parse).doubleValue();
                        }
                ));

        List<Map.Entry<OfflinePlayer, Double>> list = new LinkedList<>(topPlayers.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Collections.reverse(list);


        System.out.println(list);
        for (int i = 0; i < list.size(); i++) {
            Map.Entry<OfflinePlayer, Double> entry = list.get(i);
            if (entry.getKey().getUniqueId() == player.getUniqueId()) {
                System.out.println(entry);
                System.out.println(i);
                break;
            }

        }
    }
}
