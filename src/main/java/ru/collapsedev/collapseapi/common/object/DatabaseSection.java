package ru.collapsedev.collapseapi.common.object;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DatabaseSection {
    String type;
    MySQLSection mysqlSection;
    SQLiteSection sqliteSection;

    @AllArgsConstructor
    @Getter
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class MySQLSection {
        String host, username, password, database;
        List<String> params;
    }

    @AllArgsConstructor
    @Getter
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class SQLiteSection {
        String fileName;
        List<String> params;
    }

    private DatabaseSection(String type, MySQLSection mysqlSection, SQLiteSection sqliteSection) {
        this.type = type;
        this.mysqlSection = mysqlSection;
        this.sqliteSection = sqliteSection;
    }

    public static DatabaseSection of(ConfigurationSection section) {
        ConfigurationSection mysqlSection = section.getConfigurationSection("mysql");
        ConfigurationSection sqliteSection = section.getConfigurationSection("sqlite");

        return new DatabaseSection(
                section.getString("type").toUpperCase(),
                getMySQLSection(mysqlSection),
                getSQLiteSection(sqliteSection)
        );
    }

    private static DatabaseSection.MySQLSection getMySQLSection(ConfigurationSection section) {
        return new DatabaseSection.MySQLSection(
                section.getString("host", "localhost"),
                section.getString("username", "root"),
                section.getString("password", "root"),
                section.getString("database", "mine"),
                section.getStringList("params")
        );
    }

    private static DatabaseSection.SQLiteSection getSQLiteSection(ConfigurationSection section) {
        return new DatabaseSection.SQLiteSection(
                section.getString("file-name", "database.db"),
                section.getStringList("params")
        );
    }
}
