package ru.collapsedev.collapseapi.common.database;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DatabaseType {
    SQLITE, MYSQL, ABSTRACT;
}
