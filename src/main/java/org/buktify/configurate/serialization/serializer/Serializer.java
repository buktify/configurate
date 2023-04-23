package org.buktify.configurate.serialization.serializer;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public interface Serializer<T> {

    T deserialize(@NotNull String path, @NotNull FileConfiguration configuration);

    default void serialize(@NotNull T object, @NotNull String path, @NotNull FileConfiguration configuration) {
        configuration.set(path, object);
    }
}
