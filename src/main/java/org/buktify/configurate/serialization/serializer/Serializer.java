package org.buktify.configurate.serialization.serializer;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

/**
 * A serializer interface for converting objects to and from file configurations.
 *
 * @param <T> The type of object to be serialized.
 */
public interface Serializer<T> {

    /**
     * Deserializes an object from the specified file configuration at the given path.
     *
     * @param path          The path to the configuration value.
     * @param configuration The file configuration to deserialize from.
     * @return The deserialized object of type T.
     */
    @NotNull
    T deserialize(@NotNull String path, @NotNull FileConfiguration configuration);

    /**
     * Serializes the specified object into the file configuration at the given path.
     *
     * @param object        The object to be serialized.
     * @param path          The path to the configuration value.
     * @param configuration The file configuration to serialize into.
     */
    default void serialize(@NotNull T object, @NotNull String path, @NotNull FileConfiguration configuration) {
        configuration.set(path, object);
    }
}