package org.buktify.configurate.serialization.serializer.impl;

import org.bukkit.configuration.file.FileConfiguration;
import org.buktify.configurate.serialization.serializer.Serializer;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class DoubleSerializer implements Serializer<Double> {

    @Override
    public Double deserialize(@NotNull String path, @NotNull FileConfiguration configuration) {
        return configuration.getDouble(path);
    }
}
