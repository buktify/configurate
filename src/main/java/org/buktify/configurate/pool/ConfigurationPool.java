package org.buktify.configurate.pool;

import org.buktify.configurate.exception.ConfigurationException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

/**
 * A pool for retrieving configurations of the specified class type.
 *
 * @since 1.2.0
 */
@SuppressWarnings("unused")
public interface ConfigurationPool {

    @NotNull <T> List<T> getMappedConfigurationsList(@NotNull Function<? super Object, ? extends T> mapper);

    @NotNull
    default List<Object> getConfigurations() {
        return getMappedConfigurationsList(o -> o);
    }

    /**
     * Retrieves a configuration of the specified class type from the pool.
     *
     * @param configurationClass The class type of the configuration to retrieve.
     * @param <T>                The type parameter for the configuration.
     * @return The configuration of the specified class type.
     * @throws ConfigurationException If the configuration is not exits.
     */
    @NotNull <T> T getConfiguration(@NotNull Class<T> configurationClass) throws ConfigurationException;

    /**
     * Flushes the configuration pool, removing all configurations.
     * This operation resets the pool to its initial state.
     */
    void flush();

    /**
     * Adds a configuration to pool.
     *
     * @param configuredObject The object to be configured.
     * @throws ConfigurationException If an error occurs while adding the configuration.
     */
    void addConfiguration(@NotNull Object configuredObject) throws ConfigurationException;
}