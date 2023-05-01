package org.buktify.configurate.serialization.serializer.impl;

import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.buktify.configurate.serialization.serializer.Serializer;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ClassSerializer implements Serializer<Class<?>> {

    @Override
    @SneakyThrows(ClassNotFoundException.class)
    public Class<?> deserialize(@NotNull String path, @NotNull FileConfiguration fileConfiguration) {
        return Class.forName(fileConfiguration.getString(path));
    }

    @Override
    public void serialize(@NotNull Class<?> aClass, @NotNull String path, @NotNull FileConfiguration configuration) {
        configuration.set(path, aClass.getName());
    }
}
