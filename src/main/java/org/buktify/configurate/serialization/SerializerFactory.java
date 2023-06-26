package org.buktify.configurate.serialization;

import com.google.common.primitives.Primitives;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.file.FileConfiguration;
import org.buktify.configurate.exception.SerializationException;
import org.buktify.configurate.serialization.serializer.Serializer;
import org.buktify.configurate.serialization.serializer.impl.*;
import org.buktify.configurate.serialization.serializer.impl.bukkit.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SerializerFactory {

    List<Class<?>> primitivesList = List.of(Boolean.class, Short.class, Double.class, Byte.class, String.class, Integer.class, Character.class);
    HashMap<Class<? extends Serializer<?>>, Serializer<?>> serializerRegistry = new HashMap<>();
    TypedListSerializer typedListSerializer = new TypedListSerializer();

    public SerializerFactory() {
        registerDefaultSerializers();
    }

    public void register(@NotNull Class<? extends Serializer<?>> serializerClass) throws SerializationException {
        if (!Serializer.class.isAssignableFrom(serializerClass)) {
            throw new SerializationException(serializerClass.getSimpleName() + " must implement Serializer");
        }
        if (!serializerClass.getSimpleName().endsWith("Serializer")) {
            throw new SerializationException(serializerClass.getSimpleName() + "'s class name must ends with Serializer");
        }
        if (serializerRegistry.containsKey(serializerClass)) {
            throw new SerializationException(serializerClass.getSimpleName() + " already registered");
        }
        serializerRegistry.put(serializerClass, getSerializerInstance(serializerClass));
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
        register(MaterialSerializer.class);
        register(HashMapSerializer.class);
    }

    @SuppressWarnings({"unchecked"})
    @SneakyThrows(ClassNotFoundException.class)
    private <T> Serializer<T> getSerializer(@NotNull Class<T> type) throws SerializationException {
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

    @SneakyThrows({SerializationException.class})
    @SuppressWarnings("unchecked")
    public <T> void serialize(@NotNull Field field, @NotNull T object, @NotNull String path, @NotNull FileConfiguration configuration) {
        Class<T> objectClass = (Class<T>) object.getClass();
        if (object.getClass().isPrimitive()) {
            objectClass = Primitives.wrap(objectClass);
        }
        if (object instanceof List<?>) {
            List<?> list = (List<?>) object;
            Class<?> genericType = getFieldGenericType(field);
            if (!primitivesList.contains(genericType)) {
                serializeTypedList(object, genericType, path, configuration);
            }
            return;
        }
        getSerializer(objectClass).serialize(object, path, configuration);
    }

    @SneakyThrows(SerializationException.class)
    @SuppressWarnings("unchecked")
    public <T> T deserialize(@NotNull Field field, @NotNull Class<T> type, @NotNull String path, @NotNull FileConfiguration configuration) {
        if (type.isPrimitive()) type = Primitives.wrap(type);
        if (List.class.isAssignableFrom(type)) {
            Class<?> genericType = getFieldGenericType(field);
            if (!primitivesList.contains(genericType)) {
                return (T) deserializeTypedList(genericType, path, configuration);
            }
        }
        return getSerializer(type).deserialize(path, configuration);
    }

    @SneakyThrows({SerializationException.class})
    @SuppressWarnings("unchecked")
    private <T> void serializeTypedList(@NotNull Object list, @NotNull Class<T> genericType, @NotNull String path, @NotNull FileConfiguration configuration) {
        typedListSerializer.serialize(((List<T>) list), getSerializer(genericType), path, configuration);
    }

    @SneakyThrows(SerializationException.class)
    private <T> List<T> deserializeTypedList(@NotNull Class<T> genericType, @NotNull String path, @NotNull FileConfiguration configuration) {
        return typedListSerializer.deserialize(getSerializer(genericType), path, configuration);
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

    @SneakyThrows(ClassNotFoundException.class)
    private Class<?> getFieldGenericType(@NotNull Field field) {
        String type = field.getGenericType().getTypeName();
        int startIndex = type.indexOf("<") + 1;
        int endIndex = type.lastIndexOf(">");
        return Class.forName(type.substring(startIndex, endIndex));
    }
}
