package ru.collapsedev.collapseapi.util;

import com.cryptomorin.xseries.XSound;
import com.destroystokyo.paper.Title;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

@UtilityClass
public class PlayerUtil {

    public Title parseTitle(String[] titleArgs) {
        switch (titleArgs.length) {
            case 1: return new Title(titleArgs[0], "", 20, 20, 20);
            case 2: return new Title(titleArgs[0], titleArgs[1], 20, 20, 20);
            case 5: return new Title(titleArgs[0], titleArgs[1],
                        Integer.parseInt(titleArgs[2]),
                        Integer.parseInt(titleArgs[3]),
                        Integer.parseInt(titleArgs[4]));
            default: return new Title("None");
        }
    }

    public void sendTitle(Player player, String[] titleArgs) {
        player.sendTitle(parseTitle(titleArgs));
    }

    public void sendTitle(Player player, String title) {
        player.sendTitle(parseTitle(StringUtil.placeholdersColor(player, title).split(":")));
    }

    public void sendActionBar(Player player, String actionBar) {
        player.sendActionBar(StringUtil.placeholdersColor(player, actionBar));
    }

    public void playSound(Player player, String sound) {
        XSound.parse(sound.replace(":", ",")).soundPlayer().forPlayers(player).play();
    }

    public void playSound(List<Player> players, String sound) {
        XSound.parse(sound).soundPlayer().forPlayers(players).play();
    }

    public void sendMessage(Player player, String message) {
        player.sendMessage(StringUtil.placeholdersColor(player, message));
    }

    public void broadcast(String message) {
        Bukkit.getOnlinePlayers().forEach(all -> sendMessage(all, message));
    }

    public void sendCommand(Player player, String command) {
        player.chat("/" + command);
    }
}
