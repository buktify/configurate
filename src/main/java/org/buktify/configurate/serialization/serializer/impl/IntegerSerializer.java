package org.buktify.configurate.serialization.serializer.impl;

import org.buktify.configurate.bukkit.configuration.file.FileConfiguration;
import org.buktify.configurate.serialization.serializer.Serializer;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class IntegerSerializer implements Serializer<Integer> {

    @Override
    public @NotNull Integer deserialize(@NotNull String path, @NotNull FileConfiguration configuration) {
        return configuration.getInt(path);
    }
}
