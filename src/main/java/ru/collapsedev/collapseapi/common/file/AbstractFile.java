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
    final String configVersion;

    File file;
    FileConfiguration config;
    boolean setFields;

    @SneakyThrows
    protected AbstractFile(Plugin plugin, String fileName, String configVersion) {
        this.fileName = fileName;
        this.plugin = plugin;
        this.configVersion = configVersion;

        this.file = getFile();

        if (!file.exists()) {
            file.createNewFile();

            setFields = true;
            this.file = getFile();
        }

        load();
    }

    public File getFile() {
        return new File(plugin.getDataFolder(), fileName);
    }

    public abstract void postLoad();

    public abstract void setDefaultFields();

    public void setFields() {
        config.set("config-version", configVersion);
        setDefaultFields();
    }

    public void load() {
        this.config = YamlConfiguration.loadConfiguration(file);

        String configVersion = config.getString("config-version", null);
        if (setFields || configVersion == null || !configVersion.equals(this.configVersion)) {
            setFields();
            reload();
        }

        postLoad();
    }

    @SneakyThrows
    public void reload() {
        this.file = getFile();
        config.save(file);
        load();
    }
}
