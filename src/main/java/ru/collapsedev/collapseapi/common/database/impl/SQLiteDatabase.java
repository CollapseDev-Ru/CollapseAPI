package ru.collapsedev.collapseapi.common.database.impl;

import ru.collapsedev.collapseapi.common.database.AbstractDatabase;
import ru.collapsedev.collapseapi.api.database.Database;
import ru.collapsedev.collapseapi.common.database.DatabaseType;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class SQLiteDatabase extends AbstractDatabase {
    private final Plugin plugin;
    private final String fileName;
    private final List<String> params;
    private String url;
    private String param;

    private SQLiteDatabase(Plugin plugin, String fileName, List<String> params) {
        super(DatabaseType.SQLITE);
        this.plugin = plugin;
        this.fileName = fileName;
        this.params = params;
    }

    public static Database create(Plugin plugin, String fileName, List<String> params) {
        SQLiteDatabase db = new SQLiteDatabase(plugin, fileName, params);

        db.build();
        db.connect();
        return db;
    }

    private void build() {
        String filePath = plugin.getDataFolder().getPath();

        this.url = String.format("jdbc:sqlite:%s%s%s", filePath, File.separator, fileName);
        this.param = super.paramsBuilder(params);
    }

    @Override
    public void connect() {
        try {
            super.connection = DriverManager.getConnection(url + param);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к базе данных: " + e);
        }
    }
}
