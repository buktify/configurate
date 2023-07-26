package org.buktify.configurate.serialization.serializer.impl;

import org.buktify.configurate.bukkit.configuration.file.FileConfiguration;
import org.buktify.configurate.serialization.serializer.Serializer;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class BooleanSerializer implements Serializer<Boolean> {

    @Override
    public @NotNull Boolean deserialize(@NotNull String path, @NotNull FileConfiguration configuration) {
        return configuration.getBoolean(path);
    }
}
