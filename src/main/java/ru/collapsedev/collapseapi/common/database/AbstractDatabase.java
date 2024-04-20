package ru.collapsedev.collapseapi.common.database;

import lombok.*;
import ru.collapsedev.collapseapi.api.database.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public abstract class AbstractDatabase implements Database {
    @Getter
    private final DatabaseType databaseType;
    protected Connection connection;
    private Statement statement;

    @SneakyThrows
    @Override
    public void executeUpdate(String sql, Object... objects) {
        getStatement().executeUpdate(replaceSql(sql, objects));
    }


    @SneakyThrows
    @Override
    public void executeUpdates(List<String> sqls) {
        for (String sql : sqls) {
            getStatement().executeUpdate(sql);
        }
    }

    @SneakyThrows
    @Override
    public List<Map<String, Object>> executeQuery(String sql, Object... objects) {
        List<Map<String, Object>> result = new ArrayList<>();

        @Cleanup
        ResultSet resultSet = getStatement().executeQuery(replaceSql(sql, objects));

        while (resultSet.next()) {
            Map<String, Object> row = new HashMap<>();
            int columns = resultSet.getMetaData().getColumnCount();

            for (int i = 1; i <= columns; i++) {
                String columnName = resultSet.getMetaData().getColumnName(i);
                Object value = resultSet.getObject(i);
                row.put(columnName, value);
            }

            result.add(row);
        }

        return result;
    }


    @SneakyThrows
    @Override
    public boolean hasConnection() {
        return !(connection == null || connection.isClosed());
    }


    @SneakyThrows
    @Override
    public boolean hasStatement() {
        return !(statement == null || statement.isClosed());
    }


    @SneakyThrows
    @Override
    public Connection getConnection() {
        if (!hasConnection()) {
            connect();
        }

        return connection;
    }

    @SneakyThrows
    @Override
    public Statement getStatement() {
        if (!hasStatement()) {
            statement = getConnection().createStatement();
        }

        return statement;
    }

    protected String replaceSql(String sql, Object[] objects) {
        return objects.length != 0 ? String.format(sql.replace("?", "%s"), objects) : sql;
    }

    @SneakyThrows
    @Override
    public void close() {
        if (hasStatement()) {
            statement.close();
        }
        if (hasConnection()) {
            connection.close();
        }
    }

    protected String paramsBuilder(List<String> params) {
        return "?" + String.join("&", params);
    }
}
