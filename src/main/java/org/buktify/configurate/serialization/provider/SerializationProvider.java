package org.buktify.configurate.serialization.provider;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

/**
 * A generic interface for serializing and deserializing configuration objects.
 *
 * @param <T> The type of object to be serialized and deserialized.
 */
@SuppressWarnings("unused")
public interface SerializationProvider<T> {

    /**
     * Deserializes an object from the specified file configuration.
     *
     * @param configuration The file configuration to deserialize from.
     * @return The deserialized object of type T.
     */
    @NotNull
    T deserialize(@NotNull FileConfiguration configuration);

    /**
     * Serializes the specified object into the file configuration.
     *
     * @param object        The object to be serialized.
     * @param configuration The file configuration to serialize into.
     */
    void serialize(@NotNull T object, @NotNull FileConfiguration configuration);
}
