package dev.temez.configurate.serialization;

import dev.temez.configurate.exception.SerializationException;
import dev.temez.configurate.serialization.serializer.Serializer;
import dev.temez.configurate.serialization.serializer.impl.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SerializerFactory {

    @Getter
    List<Class<?>> primitivesList = List.of(Boolean.class, Short.class, Double.class, Byte.class, String.class, Integer.class, Character.class);

    List<String> serializerRegistry = new ArrayList<>();


    public SerializerFactory() {
        registerDefaultSerializers();
    }

    public void register(Class<?>... serializer) {
        serializerRegistry.addAll(Arrays.stream(serializer).toList().stream().map(Class::getName).toList());
    }

    private void registerDefaultSerializers() {
        register(IntegerSerializer.class);
        register(StringSerializer.class);
        register(FloatSerializer.class);
        register(DoubleSerializer.class);
        register(ListSerializer.class);
        register(LocationSerializer.class);
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
