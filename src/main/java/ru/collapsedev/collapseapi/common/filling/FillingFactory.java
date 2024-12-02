package ru.collapsedev.collapseapi.common.filling;

import ru.collapsedev.collapseapi.common.object.Points;

@FunctionalInterface
public interface FillingFactory {

    AbstractFilling create(Points points);

}
