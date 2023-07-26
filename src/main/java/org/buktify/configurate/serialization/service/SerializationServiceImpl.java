package org.buktify.configurate.serialization.service;

import com.google.common.primitives.Primitives;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.buktify.configurate.annotation.Comment;
import org.buktify.configurate.annotation.Configuration;
import org.buktify.configurate.annotation.Variable;
import org.buktify.configurate.bukkit.configuration.file.FileConfiguration;
import org.buktify.configurate.bukkit.configuration.file.YamlConfiguration;
import org.buktify.configurate.exception.ConfigurationException;
import org.buktify.configurate.exception.SerializationException;
import org.buktify.configurate.serialization.serializer.SerializerFactory;
import org.buktify.configurate.serialization.serializer.impl.TypedListSerializer;
import org.buktify.configurate.serialization.serializer.impl.provider.SerializationProvider;
import org.buktify.configurate.serialization.serializer.impl.provider.impl.DummySerializationProvider;
import org.buktify.configurate.util.TriConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class SerializationServiceImpl implements SerializationService {

    List<Class<?>> primitivesList = List.of(Boolean.class, Short.class, Double.class, Byte.class, String.class, Integer.class, Character.class);

    TypedListSerializer typedListSerializer = new TypedListSerializer();

    @NotNull
    File baseDirectory;

    @NotNull
    SerializerFactory serializerFactory;

    @Override
    @SneakyThrows
    @SuppressWarnings("all")
    public <T> void update(@NotNull Class<T> configurationClass) throws ConfigurationException, SerializationException {
        T configuration = instantiateConfigurationClass(configurationClass);
        processFields((field, path, fileConfiguration) -> {
            if (fileConfiguration.isSet(path)) return;
            field.setAccessible(true);
            serializeField(field, field.get(configuration), path, fileConfiguration);
            Comment comment = field.getAnnotation(Comment.class);
            if (comment == null) return;
            try {
                Method method = FileConfiguration.class.getMethod("setComments", String.class, List.class);
                method.invoke(fileConfiguration, path, Arrays.stream(comment.value()).collect(Collectors.toList()));
            } catch (Exception ignored) {
                log.error("Comments are not supported on your Bukkit version");
            }
        }, configuration);
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private <T> void processFields(@NotNull TriConsumer<Field, String, FileConfiguration> fieldConsumer, @NotNull T configuration) {
        Class<T> configurationClass = (Class<T>) configuration.getClass();
        Configuration configurationAnnotation = configurationClass.getAnnotation(Configuration.class);
        List<Field> configurableFields = getConfigurableFields(configurationClass);
        if (configurableFields.isEmpty()) return;
        File configurationFile = getFile(configurationAnnotation);
        FileConfiguration fileConfiguration = loadConfiguration(configurationFile);
        SerializationProvider<?> serializationProvider = getSerializationProvider(configurationAnnotation);
        if (serializationProvider == null) {
            for (Field field : configurableFields) {
                Variable variable = field.getAnnotation(Variable.class);
                field.setAccessible(true);
                fieldConsumer.accept(field, variable.value(), fileConfiguration);
            }
        }
        fileConfiguration.save(configurationFile);
    }

    @Override
    public <T> T deserialize(@NotNull Class<T> configurationClass) throws ConfigurationException, SerializationException {
        T configuration = instantiateConfigurationClass(configurationClass);
        processFields((field, path, fileConfiguration) -> {
            field.setAccessible(true);
            field.set(configuration, deserializeField(field, field.getType(), path, fileConfiguration));
        }, configuration);
        return configuration;
    }

    @NotNull
    private List<Field> getConfigurableFields(@NotNull Class<?> configurationClass) {
        return Arrays
                .stream(configurationClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Variable.class))
                .collect(Collectors.toList());
    }

    @NotNull
    @SneakyThrows
    private FileConfiguration loadConfiguration(@NotNull File file) {
        FileConfiguration fileConfiguration = new YamlConfiguration();
        fileConfiguration.load(file);
        return fileConfiguration;
    }

    @NotNull
    @SuppressWarnings("all")
    @SneakyThrows
    private File getFile(@NotNull Configuration configurationAnnotation) {
        File file = new File(baseDirectory + configurationAnnotation.filePath()
                .replaceAll("%plugin_root%", "")
                .replaceAll("%filename%", configurationAnnotation.fileName()));
        file.getParentFile().mkdirs();
        file.createNewFile();
        return file;
    }

    @Nullable
    @SneakyThrows
    private SerializationProvider<?> getSerializationProvider(@NotNull Configuration configurationAnnotation) throws ConfigurationException {
        switch (configurationAnnotation.serialization()) {
            default:
            case REFLECT: {
                return null;
            }
            case PLAIN: {
                if (configurationAnnotation.provider().equals(DummySerializationProvider.class)) {
                    throw new ConfigurationException("Please, specify configuration provider for plain serialization");
                }
                return configurationAnnotation.provider().getConstructor().newInstance();
            }
        }
    }

    @NotNull
    private <T> T instantiateConfigurationClass(@NotNull Class<T> configurationClass) {
        if (!configurationClass.isAnnotationPresent(Configuration.class)) {
            throw new ConfigurationException("Configuration " + configurationClass.getSimpleName() + " must be annotated with @Configuration");
        }
        T configuration;
        try {
            configuration = configurationClass.getDeclaredConstructor().newInstance();
        } catch (Exception ignored) {
            throw new ConfigurationException("Can't initialize " + configurationClass.getSimpleName() + ", configuration class must have no arguments constructor");
        }
        return configuration;
    }

    @SneakyThrows({SerializationException.class})
    @SuppressWarnings("unchecked")
    public <T> void serializeField(@NotNull Field field, @NotNull T value, @NotNull String path, @NotNull FileConfiguration configuration) {
        Class<T> objectClass = (Class<T>) value.getClass();
        if (objectClass.isPrimitive()) {
            objectClass = Primitives.wrap(objectClass);
        }
        if (value instanceof List<?>) {
            Class<?> genericType = getFieldGenericType(field);
            if (!primitivesList.contains(genericType)) {
                serializeTypedList(value, genericType, path, configuration);
                return;
            }
        }
        serializerFactory.getSerializer(objectClass).serialize(value, path, configuration);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    private <T> T deserializeField(@NotNull Field field, @NotNull Class<T> type, @NotNull String path, @NotNull FileConfiguration configuration) {
        if (type.isPrimitive()) type = Primitives.wrap(type);
        field.setAccessible(true);
        if (List.class.isAssignableFrom(type)) {
            Class<?> genericType = getFieldGenericType(field);
            if (!primitivesList.contains(genericType)) {
                return (T) deserializeTypedList(genericType, path, configuration);
            }
        }
        return serializerFactory.getSerializer(type).deserialize(path, configuration);
    }

    @NotNull
    @SneakyThrows(ClassNotFoundException.class)
    private Class<?> getFieldGenericType(@NotNull Field field) {
        String type = field.getGenericType().getTypeName();
        int startIndex = type.indexOf("<") + 1;
        int endIndex = type.lastIndexOf(">");
        type = type.substring(startIndex, endIndex);
        return Class.forName(type);
    }

    @SneakyThrows({SerializationException.class})
    @SuppressWarnings("unchecked")
    private <T> void serializeTypedList(@NotNull Object list, @NotNull Class<T> genericType, @NotNull String path, @NotNull FileConfiguration configuration) {
        typedListSerializer.serialize(((List<T>) list), serializerFactory.getSerializer(genericType), path, configuration);
    }

    @SneakyThrows(SerializationException.class)
    private <T> List<T> deserializeTypedList(@NotNull Class<T> genericType, @NotNull String path, @NotNull FileConfiguration configuration) {
        return typedListSerializer.deserialize(serializerFactory.getSerializer(genericType), path, configuration);
    }
}
