package org.buktify.configurate.serialization.service;

import org.buktify.configurate.exception.ConfigurationException;
import org.buktify.configurate.exception.SerializationException;
import org.jetbrains.annotations.NotNull;

/**
 * A service interface for serialization and deserialization operations.
 *
 * @since 1.2.0
 */
public interface SerializationService {

    /**
     * Updates the file configuration with the values from the specified configuration object.
     *
     * @param configuration The configuration object containing updated values.
     * @param <T>           The type parameter for the configuration object.
     * @throws ConfigurationException If an error occurs during the configuration update.
     * @throws SerializationException If an error occurs during serialization.
     */
    <T> void update(@NotNull Class<T> configuration) throws ConfigurationException, SerializationException;

    /**
     * Deserializes a configuration object of the specified class from a file configuration.
     *
     * @param configurationClass The class of the configuration object to be deserialized.
     * @param <T>                The type parameter for the configuration object.
     * @return The deserialized configuration object.
     * @throws ConfigurationException If an error occurs during deserialization.
     * @throws SerializationException If an error occurs during deserialization.
     */
    <T> T deserialize(@NotNull Class<T> configurationClass) throws ConfigurationException, SerializationException;
}
