package ru.collapsedev.collapseapi.common.object;

import lombok.Data;

@Data
public class Pair<F, S> {
    private final F first;
    private final S second;

    public static <F, S> Pair<F, S> of(F first, S second) {
        return new Pair<>(first, second);
    }
}
