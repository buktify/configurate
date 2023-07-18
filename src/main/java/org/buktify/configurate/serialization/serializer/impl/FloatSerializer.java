package org.buktify.configurate.serialization.serializer.impl;

import org.bukkit.configuration.file.FileConfiguration;
import org.buktify.configurate.serialization.serializer.Serializer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings("unused")
public final class FloatSerializer implements Serializer<Float> {
    @Override
    public @NotNull Float deserialize(@NotNull String path, @NotNull FileConfiguration configuration) {
        return (Float) Objects.requireNonNull(configuration.get(path));
    }
}
