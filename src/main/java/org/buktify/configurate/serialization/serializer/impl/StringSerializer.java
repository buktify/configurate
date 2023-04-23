package org.buktify.configurate.serialization.serializer.impl;

import org.bukkit.configuration.file.FileConfiguration;
import org.buktify.configurate.serialization.serializer.Serializer;
import org.jetbrains.annotations.NotNull;

public final class StringSerializer implements Serializer<String> {

    @Override
    public String deserialize(@NotNull String path, @NotNull FileConfiguration configuration) {
        return configuration.getString(path);
    }

}
