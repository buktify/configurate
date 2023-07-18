package org.buktify.configurate.serialization.serializer.impl;

import org.bukkit.configuration.file.FileConfiguration;
import org.buktify.configurate.serialization.serializer.Serializer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings("unused")
public final class StringSerializer implements Serializer<String> {

    @Override
    public @NotNull String deserialize(@NotNull String path, @NotNull FileConfiguration configuration) {
        return Objects.requireNonNull(configuration.getString(path));
    }
}
