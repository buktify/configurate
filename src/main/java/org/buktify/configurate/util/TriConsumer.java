package org.buktify.configurate.util;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface TriConsumer<T, U, K> {

    void accept(T t, U u, K k) throws IllegalAccessException;

    default TriConsumer<T, U, K> andThen(@NotNull TriConsumer<? super T, ? super U, ? super K> after) {
        return (t, u, k) -> {
            accept(t, u, k);
            after.accept(t, u, k);
        };
    }
}