package org.buktify.configurate.serialization.serializer.impl;

import org.bukkit.configuration.file.FileConfiguration;
import org.buktify.configurate.serialization.serializer.Serializer;
import org.jetbrains.annotations.NotNull;

public class BooleanSerializer implements Serializer<Boolean> {
    @Override
    public Boolean deserialize(@NotNull String path, @NotNull FileConfiguration configuration) {
        return configuration.getBoolean(path);
    }
}
