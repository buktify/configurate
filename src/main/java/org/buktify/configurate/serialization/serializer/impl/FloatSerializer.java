package org.buktify.configurate.serialization.serializer.impl;

import org.bukkit.configuration.file.FileConfiguration;
import org.buktify.configurate.serialization.serializer.Serializer;
import org.jetbrains.annotations.NotNull;

public final class FloatSerializer implements Serializer<Float> {
    @Override
    public Float deserialize(@NotNull String path, @NotNull FileConfiguration configuration) {
        return (Float) configuration.get(path);
    }
}
