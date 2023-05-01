package org.buktify.configurate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.buktify.configurate.annotation.Comment;
import org.buktify.configurate.annotation.Configuration;
import org.buktify.configurate.annotation.Variable;
import org.buktify.configurate.exception.ConfigurationException;
import org.buktify.configurate.exception.SerializationException;
import org.buktify.configurate.serialization.SerializerFactory;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A service for managing and applying configurations.
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Log4j2
@SuppressWarnings("unused")
public class ConfigurationService {

    @Getter
    ConfigurationPool configurationPool = new ConfigurationPool();
    List<Class<?>> loadedConfigurations = new ArrayList<>();
    SerializerFactory serializerFactory = new SerializerFactory();
    @NonFinal
    File baseDirectory;

    /**
     * Sets the root directory for configuration files.
     *
     * @param baseDirectory the root directory for configuration files
     * @return the configuration service
     */
    public ConfigurationService rootDirectory(@NotNull File baseDirectory) {
        this.baseDirectory = baseDirectory;
        return this;
    }

    /**
     * Registers serializers for the specified classes.
     *
     * @param serializers the classes to register serializers for
     * @return the configuration service
     */
    @SuppressWarnings("unused")
    @SneakyThrows(SerializationException.class)
    public ConfigurationService registerSerializers(@NotNull Class<?>... serializers) {
        serializerFactory.register(serializers);
        return this;
    }

    /**
     * Registers a configuration objects for future processing.
     *
     * @param objects the configuration object to register
     * @return the configuration service
     */
    public ConfigurationService registerConfigurations(@NotNull Class<?>... objects) {
        loadedConfigurations.addAll(Arrays.stream(objects).toList());
        return this;
    }

    /**
     * Applies the registered configurations by processing and creating default configuration files if needed.
     */
    @SneakyThrows(ConfigurationException.class)
    public void apply() {
        if (baseDirectory == null)
            throw new ConfigurationException("Root directory is null, please specify it before usage");
        for (Class<?> configuration : loadedConfigurations) {
            processConfiguration(configuration);
        }
    }

    /**
     * Processes a configurationClass object by deserializing annotated fields and gathering data from,
     * linked in configurationClass, file.
     *
     * @param configurationClass the configurationClass object to process
     * @throws ConfigurationException if the configurationClass object is not annotated with @Configuration
     */
    @SneakyThrows({IOException.class, InvalidConfigurationException.class, IllegalAccessException.class})
    private void processConfiguration(@NotNull Class<?> configurationClass) throws ConfigurationException {
        Object configuration;
        try {
            configuration = configurationClass.getDeclaredConstructor().newInstance();
        } catch (Exception ignored) {
            throw new ConfigurationException("Can't initialize " + configurationClass.getSimpleName() + ", configuration class must have no arguments constructor");
        }
        Configuration configurationAnnotation = configurationClass.getAnnotation(Configuration.class);
        if (configurationAnnotation == null)
            throw new ConfigurationException("Configuration " + configuration.getClass().getSimpleName() + " must be annotated with @Configuration");
        List<Field> fields = Arrays.stream(configuration.getClass().getDeclaredFields()).toList();
        File file = new File(baseDirectory + configurationAnnotation.filePath().replaceAll("%plugin_root%", "").replaceAll("%filename%", configurationAnnotation.fileName()));
        updateConfiguration(configuration, file);
        FileConfiguration fileConfiguration = new YamlConfiguration();
        fileConfiguration.load(file);
        for (Field field : fields) {
            Variable variable = field.getAnnotation(Variable.class);
            if (variable == null) continue;
            field.setAccessible(true);
            if (field.getType().isAssignableFrom(List.class)) {
                Class<?> genericType = getGenericType(field);
                if (!serializerFactory.getPrimitivesList().contains(genericType)) {
                    field.set(configuration, serializerFactory.deserializeTypedList(genericType, variable.value(), fileConfiguration));
                    continue;
                }
            }
            field.set(configuration, serializerFactory.deserialize(field.getType(), variable.value(), fileConfiguration));
        }
        configurationPool.put(configuration);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SneakyThrows({IOException.class, InvalidConfigurationException.class, IllegalAccessException.class})
    private void updateConfiguration(@NotNull Object configuration, @NotNull File file) {
        file.getParentFile().mkdirs();
        file.createNewFile();
        FileConfiguration fileConfiguration = new YamlConfiguration();
        fileConfiguration.load(file);
        for (Field field : configuration.getClass().getDeclaredFields()) {
            Variable variable = field.getAnnotation(Variable.class);
            if (variable == null) continue;
            if (fileConfiguration.isSet(variable.value())) continue;
            field.setAccessible(true);

            if (field.getType().isAssignableFrom(List.class)) {
                Class<?> genericType = getGenericType(field);
                if (!serializerFactory.getPrimitivesList().contains(genericType)) {
                    serializerFactory.serializeTypedList(field.get(configuration), genericType, variable.value(), fileConfiguration);
                    Comment comment = field.getAnnotation(Comment.class);
                    if (comment == null) continue;
                    try {
                        fileConfiguration.setComments(variable.value(), Arrays.stream(comment.value()).toList());
                    } catch (Exception ignored) {
                        log.warn("Comments are not supported on your Bukkit version");
                    }
                    continue;
                }
            }
            serializerFactory.serialize(field.get(configuration), variable.value(), fileConfiguration);
            Comment comment = field.getAnnotation(Comment.class);
            if (comment == null) continue;
            fileConfiguration.setComments(variable.value(), Arrays.stream(comment.value()).toList());

        }
        fileConfiguration.save(file);
    }

    @SneakyThrows(ClassNotFoundException.class)
    private Class<?> getGenericType(@NotNull Field field) {
        String type = field.getGenericType().getTypeName();
        int startIndex = type.indexOf("<") + 1;
        int endIndex = type.lastIndexOf(">");
        return Class.forName(type.substring(startIndex, endIndex));
    }
}
