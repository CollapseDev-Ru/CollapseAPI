package ru.collapsedev.collapseapi.common.object;

@FunctionalInterface
public interface MultiFunction<A, B, R> {
    R apply(A s, B a);
}
