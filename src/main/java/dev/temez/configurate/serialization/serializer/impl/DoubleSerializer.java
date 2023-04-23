package dev.temez.configurate.serialization.serializer.impl;

import dev.temez.configurate.serialization.serializer.Serializer;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public final class DoubleSerializer implements Serializer<Double> {

    @Override
    public Double deserialize(@NotNull String path, @NotNull FileConfiguration configuration) {
        return configuration.getDouble(path);
    }
}
