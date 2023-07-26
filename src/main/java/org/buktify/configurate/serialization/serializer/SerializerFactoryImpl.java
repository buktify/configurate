package org.buktify.configurate.serialization.serializer;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.buktify.configurate.exception.ConfigurationException;
import org.buktify.configurate.exception.SerializationException;
import org.buktify.configurate.serialization.serializer.impl.*;
import org.buktify.configurate.serialization.serializer.impl.bukkit.LocationSerializer;
import org.buktify.configurate.serialization.serializer.impl.bukkit.MaterialSerializer;
import org.buktify.configurate.serialization.serializer.impl.bukkit.WorldSerializer;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SerializerFactoryImpl implements SerializerFactory {

    HashMap<Class<? extends Serializer<?>>, Serializer<?>> serializerRegistry = new HashMap<>();

    public SerializerFactoryImpl() {
        registerDefaultSerializers();
    }

    @Override
    @SuppressWarnings("all")
    @SneakyThrows
    public <T> Serializer<T> getSerializer(@NotNull Class<T> type) throws SerializationException {
        if (List.class.isAssignableFrom(type)) {
            return (Serializer<T>) getSerializerInstance(ListSerializer.class);
        }
        String serializerClassName = type.getSimpleName() + "Serializer";
        for (String serializer : serializerRegistry.keySet().stream().map(Class::getName).collect(Collectors.toList())) {
            if (serializer.contains(serializerClassName)) {
                return (Serializer<T>) getSerializerInstance(Class.forName(serializer));
            }
        }
        throw new SerializationException("Serializer " + serializerClassName + " not found");
    }

    @SneakyThrows(SerializationException.class)
    private void registerDefaultSerializers() {
        registerSerializers(
                List.of(
                        IntegerSerializer.class,
                        BooleanSerializer.class,
                        StringSerializer.class,
                        FloatSerializer.class,
                        DoubleSerializer.class,
                        ListSerializer.class,
                        LocationSerializer.class,
                        ClassSerializer.class,
                        WorldSerializer.class,
                        MaterialSerializer.class,
                        HashMapSerializer.class
                )
        );
    }

    @Override
    public final void registerSerializers(@NotNull Collection<Class<? extends Serializer<?>>> serializers) throws ConfigurationException {
        for (Class<? extends Serializer<?>> serializerClass : serializers) {
            if (!Serializer.class.isAssignableFrom(serializerClass)) {
                throw new ConfigurationException(serializerClass.getSimpleName() + " must implement Serializer");
            }
            if (!serializerClass.getSimpleName().endsWith("Serializer")) {
                throw new ConfigurationException(serializerClass.getSimpleName() + "'s class name must ends with Serializer");
            }
            if (serializerRegistry.containsKey(serializerClass)) {
                throw new ConfigurationException(serializerClass.getSimpleName() + " already registered");
            }
            serializerRegistry.put(serializerClass, getSerializerInstance(serializerClass));
        }
    }

    @SuppressWarnings({"unchecked"})
    @SneakyThrows({InvocationTargetException.class, InstantiationException.class, IllegalAccessException.class, IllegalArgumentException.class})
    private <T> Serializer<T> getSerializerInstance(@NotNull Class<T> type) throws SerializationException {
        try {
            return (Serializer<T>) type.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new SerializationException(type.getSimpleName() + " must have no arguments constructor");
        }
    }
}
