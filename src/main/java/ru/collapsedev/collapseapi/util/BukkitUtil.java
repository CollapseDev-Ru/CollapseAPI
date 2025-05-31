package ru.collapsedev.collapseapi.util;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import ru.collapsedev.collapseapi.APILoader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

@UtilityClass
public class BukkitUtil {

    public void runSync(Plugin plugin, Runnable runnable) {
        Bukkit.getScheduler().runTask(plugin, runnable);
    }
    public void runSync(Runnable runnable) {
        runSync(APILoader.getInstance(), runnable);
    }


    public void runAsync(Plugin plugin, Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }
    public void runAsync(Runnable runnable) {
        runAsync(APILoader.getInstance(), runnable);
    }


    public void runTaskAsync(Plugin plugin, Runnable runnable, long delay, long period) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, period);
    }
    public void runTaskAsync(Plugin plugin, Runnable runnable, long period) {
        runTaskAsync(plugin, runnable, 0, period);
    }
    public void runTaskAsync(Runnable runnable, long period) {
        runTaskAsync(APILoader.getInstance(), runnable, 0, period);
    }
    public void runTaskAsync(Plugin plugin, Runnable runnable) {
        runTaskAsync(plugin, runnable, 0);
    }
    public void runTaskAsync(Runnable runnable) {
        runTaskAsync(APILoader.getInstance(), runnable);
    }


    public void runTaskSync(Plugin plugin, Runnable runnable, long delay, long period) {
        Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, period);
    }
    public void runTaskSync(Plugin plugin, Runnable runnable, long period) {
        runTaskSync(plugin, runnable, 0, period);
    }
    public void runTaskSync(Runnable runnable, long period) {
        runTaskSync(APILoader.getInstance(), runnable, 0, period);
    }
    public void runTaskSync(Plugin plugin, Runnable runnable) {
        runTaskSync(plugin, runnable, 0);
    }
    public void runTaskSync(Runnable runnable) {
        runTaskSync(APILoader.getInstance(), runnable);
    }


    public void runLaterAsync(Plugin plugin, Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
    }
    public void runLaterAsync(Runnable runnable, long delay) {
        runLaterAsync(APILoader.getInstance(), runnable, delay);
    }
    public void runLaterAsync(Plugin plugin, Runnable runnable) {
        runLaterAsync(plugin, runnable, 20);
    }
    public void runLaterAsync(Runnable runnable) {
        runLaterAsync(APILoader.getInstance(), runnable, 20);
    }


    public void runLaterSync(Plugin plugin, Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
    }
    public void runLaterSync(Runnable runnable, long delay) {
        runLaterSync(APILoader.getInstance(), runnable, delay);
    }
    public void runLaterSync(Plugin plugin, Runnable runnable) {
        runLaterSync(plugin, runnable, 20);
    }
    public void runLaterSync(Runnable runnable) {
        runLaterSync(APILoader.getInstance(), runnable, 20);
    }

    public void executeCommand(String command) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    public void executeCommandPlaceholdered(Player player, String command) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), StringUtil.applyPlaceholders(player, command));
    }
}
