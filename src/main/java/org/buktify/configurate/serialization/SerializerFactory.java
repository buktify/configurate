package org.buktify.configurate.serialization;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.file.FileConfiguration;
import org.buktify.configurate.exception.SerializationException;
import org.buktify.configurate.serialization.serializer.Serializer;
import org.buktify.configurate.serialization.serializer.impl.*;
import org.buktify.configurate.serialization.serializer.impl.bukkit.ItemStackSerializer;
import org.buktify.configurate.serialization.serializer.impl.bukkit.LocationSerializer;
import org.buktify.configurate.serialization.serializer.impl.bukkit.WorldSerializer;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SerializerFactory {

    @Getter
    List<Class<?>> primitivesList = List.of(Boolean.class, Short.class, Double.class, Byte.class, String.class, Integer.class, Character.class);

    List<String> serializerRegistry = new ArrayList<>();


    public SerializerFactory() {
        registerDefaultSerializers();
    }

    public void register(@NotNull Class<?>... serializers) throws SerializationException {
        for (Class<?> serializerClass : serializers) {
            if (!Serializer.class.isAssignableFrom(serializerClass)) {
                throw new SerializationException(serializerClass.getSimpleName() + " must implement Serializer");
            }
            if (!serializerClass.getSimpleName().endsWith("Serializer")) {
                throw new SerializationException(serializerClass.getSimpleName() + "'s class name must ends with Serializer");
            }
            if (serializerRegistry.contains(serializerClass.getName())) {
                throw new SerializationException(serializerClass.getSimpleName() + " already registered");
            }
            serializerRegistry.add(serializerClass.getName());
        }
    }

    @SneakyThrows(SerializationException.class)
    private void registerDefaultSerializers() {
        register(IntegerSerializer.class);
        register(BooleanSerializer.class);
        register(StringSerializer.class);
        register(FloatSerializer.class);
        register(DoubleSerializer.class);
        register(ListSerializer.class);
        register(LocationSerializer.class);
        register(ClassSerializer.class);
        register(WorldSerializer.class);
        register(ItemStackSerializer.class);
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows({SerializationException.class})
    public <T> void serialize(@NotNull T object, @NotNull String path, @NotNull FileConfiguration configuration) {
        getSerializer((Class<T>) object.getClass()).serialize(object, path, configuration);
    }

    @SneakyThrows(SerializationException.class)
    public <T> T deserialize(@NotNull Class<T> type, @NotNull String path, @NotNull FileConfiguration configuration) {
        return getSerializer(type).deserialize(path, configuration);
    }

    @SneakyThrows({SerializationException.class})
    @SuppressWarnings("unchecked")
    public <T> void serializeTypedList(@NotNull Object list, @NotNull Class<T> genericType, @NotNull String path, @NotNull FileConfiguration configuration) {
        new TypedListSerializer().serialize(((List<T>) list), getSerializer(genericType), path, configuration);
    }

    @SneakyThrows(SerializationException.class)
    public <T> List<T> deserializeTypedList(@NotNull Class<T> genericType, @NotNull String path, @NotNull FileConfiguration configuration) {
        return new TypedListSerializer().deserialize(getSerializer(genericType), path, configuration);
    }

    @SuppressWarnings({"unchecked"})
    @SneakyThrows(ClassNotFoundException.class)
    private <T> Serializer<T> getSerializer(@NotNull Class<T> type) throws SerializationException {
        if (type.getSimpleName().contains("List")) {
            return (Serializer<T>) getSerializerInstance(ListSerializer.class);
        }
        String serializerClassName = type.getSimpleName() + "Serializer";
        for (String serializer : serializerRegistry) {
            if (serializer.contains(serializerClassName)) {
                return (Serializer<T>) getSerializerInstance(Class.forName(serializer));
            }
        }
        throw new SerializationException("Serializer " + serializerClassName + " not found");
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
