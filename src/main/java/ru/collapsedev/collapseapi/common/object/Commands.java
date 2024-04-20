package ru.collapsedev.collapseapi.common.object;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Commands {
    private static final String format = "{%s}";
    List<String> commands;

    public Commands(List<String> commands) {
        this.commands = commands;
    }

    public void execute(Map<String, Object> placeholders) {
        commands.forEach(cmd -> {
            AtomicReference<String> tmpCmd = new AtomicReference<>(cmd);

            placeholders.forEach((key, value) -> {
                tmpCmd.updateAndGet(currentCmd -> currentCmd.replace(
                        String.format(format, key), String.valueOf(value))
                );
            });

            dispatchCommand(tmpCmd.get());
        });
    }

    private void dispatchCommand(String command) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }
}
