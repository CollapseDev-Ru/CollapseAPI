package ru.collapsedev.collapseapi.common.file;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.apache.commons.io.FileExistsException;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.Yaml;

import java.io.File;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AbstractFile {

    final String fileName;
    final Plugin plugin;

    File file;
    FileConfiguration config;

    @SneakyThrows
    protected AbstractFile(Plugin plugin, String fileName) {
        this.fileName = fileName;
        this.plugin = plugin;

        this.file = getFile();

        if (!file.exists()) {
            plugin.saveResource(fileName, false);

            this.file = getFile();
        }

        load();
    }

    public File getFile() {
        return new File(plugin.getDataFolder(), fileName);
    }

    public abstract void postLoad();


    public void load() {
        this.config = YamlConfiguration.loadConfiguration(file);
        postLoad();
    }

    @SneakyThrows
    public void reload() {
        this.file = getFile();
        config.save(file);
        load();
    }
}
