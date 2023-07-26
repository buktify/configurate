package org.buktify.configurate.serialization.serializer;

import org.buktify.configurate.exception.ConfigurationException;
import org.buktify.configurate.exception.SerializationException;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * A factory interface for creating serializers and registering serializer classes.
 *
 * @since 1.2.0
 */
public interface SerializerFactory {

    /**
     * Retrieves a serializer for the specified type.
     *
     * @param type The type for which the serializer is requested.
     * @param <T>  The type parameter.
     * @return The serializer for the specified type.
     */
    <T> Serializer<T> getSerializer(@NotNull Class<T> type) throws SerializationException;

    /**
     * Registers serializers for the specified Serializer classes.
     *
     * @param serializers The Serializer classes to register.
     * @throws ConfigurationException if an error occurs while registering the serializers.
     */
    @NotNull
    @SuppressWarnings("all")
    void registerSerializers(@NotNull Collection<Class<? extends Serializer<?>>> serializers) throws ConfigurationException;
}
