package org.buktify.configurate;

import org.buktify.configurate.exception.ConfigurationException;
import org.buktify.configurate.exception.SerializationException;
import org.buktify.configurate.pool.ConfigurationPool;
import org.buktify.configurate.serialization.serializer.Serializer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * A service interface for managing configurations.
 *
 * @since 1.2.0
 */
@SuppressWarnings("unused")
public interface ConfigurationService {

    /**
     * Retrieves the configuration pool associated with this service.
     *
     * @return The configuration pool.
     */
    @NotNull
    ConfigurationPool getConfigurationPool();

    /**
     * Registers serializers for the specified Serializer classes.
     *
     * @param serializers The Serializer classes to register.
     * @return The updated ConfigurationService.
     */
    @NotNull
    @SuppressWarnings("all")
    ConfigurationService registerSerializers(@NotNull Class<? extends Serializer<?>>... serializers) throws ConfigurationException;


    /**
     * Registers serializers for the specified Serializer classes.
     *
     * @param serializers The Serializer classes to register.
     * @return The updated ConfigurationService.
     */
    @NotNull
    @SuppressWarnings("all")
    ConfigurationService registerSerializers(@NotNull Collection<Class<? extends Serializer<?>>> serializers) throws ConfigurationException;


    /**
     * Registers configurations for the specified classes.
     *
     * @param objects The classes representing the configurations to register.
     * @return The updated ConfigurationService.
     */
    @NotNull
    ConfigurationService registerConfigurations(@NotNull Class<?>... objects);

    /**
     * Registers configurations for the specified classes.
     *
     * @param objects The classes representing the configurations to register.
     * @return The updated ConfigurationService.
     */
    @NotNull
    ConfigurationService registerConfigurations(@NotNull Collection<Class<?>> objects);

    /**
     * Processes the registered configurations and returns the updated ConfigurationService.
     *
     * @return The updated ConfigurationService.
     * @throws SerializationException If an error occurs during serialization.
     * @throws ConfigurationException If an error occurs during the configuration process.
     */
    @NotNull
    @SuppressWarnings("all")
    ConfigurationService apply() throws SerializationException, ConfigurationException;

}