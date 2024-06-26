package ru.collapsedev.collapseapi.util;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class CooldownUtil {
    private final Table<UUID, String, Long> cooldowns = HashBasedTable.create();

    public static void setCooldown(UUID uuid, String type, long duration) {
        long endTime = System.currentTimeMillis() + duration;
        cooldowns.put(uuid, type, endTime);
    }

    public boolean isCooldown(UUID uuid, String type) {
        Long endTime = cooldowns.get(uuid, type);
        if (endTime == null) {
            return false;
        }
        if (System.currentTimeMillis() >= endTime) {
            cooldowns.remove(uuid, type);
            return false;
        }
        return true;
    }

    public long getCooldownTime(UUID uuid, String type) {
        Long endTime = cooldowns.get(uuid, type);
        if (endTime == null) {
            return 0;
        }
        long remainingTime = endTime - System.currentTimeMillis();
        if (remainingTime <= 0) {
            cooldowns.remove(uuid, type);
            return 0;
        }
        return remainingTime;
    }

    public void removeCooldown(UUID uuid, String type) {
        cooldowns.remove(uuid, type);
    }
}