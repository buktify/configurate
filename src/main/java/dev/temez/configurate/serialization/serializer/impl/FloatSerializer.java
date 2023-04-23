package dev.temez.configurate.serialization.serializer.impl;

import dev.temez.configurate.serialization.serializer.Serializer;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public final class FloatSerializer implements Serializer<Float> {
    @Override
    public Float deserialize(@NotNull String path, @NotNull FileConfiguration configuration) {
        return (Float) configuration.get(path);
    }
}
