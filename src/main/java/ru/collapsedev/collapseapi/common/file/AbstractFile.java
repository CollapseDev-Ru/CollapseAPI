package ru.collapsedev.collapseapi.common.file;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import ru.collapsedev.collapseapi.common.object.MapAccessor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class AbstractFile {

    String fileName;
    Plugin plugin;

    File file;
    Yaml yaml;

    @NonFinal
    boolean first;
    @NonFinal
    FileConfiguration config;

    public AbstractFile(Plugin plugin, String fileName) {
        this(plugin, "", fileName);
    }

    @SneakyThrows
    public AbstractFile(Plugin plugin, String middleFolders, String fileName) {
        this.fileName = fileName;
        this.plugin = plugin;
        this.yaml = new Yaml(new SafeConstructor());

        middleFolders += middleFolders.isEmpty() ? "" : File.separator;

        String dataFolder = plugin.getDataFolder().getPath() + File.separator;
        Path filePath = Paths.get(dataFolder + middleFolders + fileName);

        if (!Files.exists(filePath)) {
            try {
                plugin.saveResource(middleFolders + fileName, false);
            } catch (Exception e) {
                Path parentDir = filePath.getParent();
                if (parentDir != null && !Files.exists(parentDir)) {
                    Files.createDirectories(parentDir);
                }

                Files.createFile(filePath);
            }
            this.first = true;
        }

        this.file = filePath.toFile();

        load();
    }

    public abstract void postLoad();

    public void load() {
        this.config = YamlConfiguration.loadConfiguration(file);
        if (first) {
            firstLoad();
        }
        postLoad();
    }

    private void firstLoad() {
        setFields();
        first = false;
        reload();
    }

    public void setFields() {
    }

    @SneakyThrows
    public void reload() {
        config.save(file);
        load();
    }

    public Map<String, Object> getCleanedValues() {
        return yaml.load(config.saveToString());
    }

    public MapAccessor getAccessor() {
        return MapAccessor.of(getCleanedValues());
    }

}
