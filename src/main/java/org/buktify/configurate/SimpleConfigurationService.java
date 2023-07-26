package org.buktify.configurate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.buktify.configurate.exception.ConfigurationException;
import org.buktify.configurate.exception.SerializationException;
import org.buktify.configurate.pool.ConfigurationPool;
import org.buktify.configurate.pool.ConfigurationPoolImpl;
import org.buktify.configurate.serialization.serializer.Serializer;
import org.buktify.configurate.serialization.serializer.SerializerFactory;
import org.buktify.configurate.serialization.serializer.SerializerFactoryImpl;
import org.buktify.configurate.serialization.service.SerializationService;
import org.buktify.configurate.serialization.service.SerializationServiceImpl;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SimpleConfigurationService implements ConfigurationService {


    List<Class<?>> registeredConfigurations = new ArrayList<>();

    @Getter
    ConfigurationPool configurationPool = new ConfigurationPoolImpl();

    SerializerFactory serializerFactory = new SerializerFactoryImpl();

    @NotNull
    SerializationService serializationService;

    public SimpleConfigurationService(@NotNull File baseDirectory) {
        this.serializationService = new SerializationServiceImpl(baseDirectory, serializerFactory);
    }

    @Override
    @SafeVarargs
    public final @NotNull ConfigurationService registerSerializers(@NotNull Class<? extends Serializer<?>>... serializers) {
        registerSerializers(Arrays.stream(serializers).collect(Collectors.toList()));
        return this;
    }

    @Override
    public @NotNull ConfigurationService registerSerializers(@NotNull Collection<Class<? extends Serializer<?>>> serializers) throws ConfigurationException {
        serializerFactory.registerSerializers(serializers);
        return this;
    }

    @Override
    public @NotNull ConfigurationService registerConfigurations(@NotNull Class<?>... objects) {
        registerConfigurations(Arrays.stream(objects).collect(Collectors.toList()));
        return this;
    }

    @Override
    public @NotNull ConfigurationService registerConfigurations(@NotNull Collection<Class<?>> objects) {
        registeredConfigurations.addAll(objects);
        return this;
    }

    @Override
    public @NotNull ConfigurationService apply() throws SerializationException, ConfigurationException {
        for (Class<?> registeredConfiguration : registeredConfigurations) {
            serializationService.update(registeredConfiguration);
            configurationPool.addConfiguration(serializationService.deserialize(registeredConfiguration));
        }
        return this;
    }
}
