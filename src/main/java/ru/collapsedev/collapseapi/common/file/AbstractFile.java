package ru.collapsedev.collapseapi.common.file;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import ru.collapsedev.collapseapi.util.ObjectUtil;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AbstractFile {

    final String filePath;
    final Plugin plugin;

    final File dataFolder;
    final File file;

    boolean first;

    FileConfiguration config;

    protected AbstractFile(Plugin plugin, String filePath) {
        this(plugin, null, filePath);
    }

    @SneakyThrows
    protected AbstractFile(Plugin plugin, String middlePath, String filePath) {
        this.filePath = filePath;
        this.plugin = plugin;

        if (middlePath != null) {
            this.dataFolder = new File(plugin.getDataFolder().getPath() + File.separator + middlePath);
        } else {
            this.dataFolder = plugin.getDataFolder();
        }

        this.file = new File(dataFolder, filePath);

        if (!file.exists()) {
            saveResource(filePath);
            this.first = true;
        }

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

    @SneakyThrows
    private void saveResource(String filePath) {
        InputStream in = getResource(filePath);
        Path path = Paths.get(dataFolder + File.separator + filePath);

        if (in == null) {
            Path parentDir = path.getParent();

            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }

            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            return;
        }

        FileOutputStream out = new FileOutputStream(path.toFile());

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }

        in.close();
        out.close();
    }

    private InputStream getResource(String filename) {
        try {
            URL url = plugin.getClass().getClassLoader().getResource(filename);
            if (url == null) {
                return null;
            }

            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException e) {
            return null;
        }
    }
}
