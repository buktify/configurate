package dev.temez.configurate.serialization.serializer.impl;

import dev.temez.configurate.serialization.serializer.Serializer;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public final class IntegerSerializer implements Serializer<Integer> {

    @Override
    public Integer deserialize(@NotNull String path, @NotNull FileConfiguration configuration) {
        return configuration.getInt(path);
    }
}
