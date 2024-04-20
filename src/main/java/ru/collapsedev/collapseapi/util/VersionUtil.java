package ru.collapsedev.collapseapi.util;

import org.bukkit.Bukkit;

public class VersionUtil {
    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + getServerVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Class<?> getCraftClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + getServerVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String getServerVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }
}
