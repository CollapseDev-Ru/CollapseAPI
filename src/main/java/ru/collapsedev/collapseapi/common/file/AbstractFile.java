package ru.collapsedev.collapseapi.common.file;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AbstractFile {

    final String fileName;
    final Plugin plugin;
    final File file;
    FileConfiguration config;

    public AbstractFile(Plugin plugin, String fileName) {
        this.fileName = fileName;
        this.plugin = plugin;

        this.file = new File(plugin.getDataFolder(), fileName);

        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }

        load();
    }

    public abstract void postLoad();

    public void load() {
        this.config = YamlConfiguration.loadConfiguration(file);
        postLoad();
    }

    @SneakyThrows
    public void reload() {
        config.save(file);
        load();
    }
}
