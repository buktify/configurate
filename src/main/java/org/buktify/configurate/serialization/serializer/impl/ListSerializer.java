package org.buktify.configurate.serialization.serializer.impl;

import org.bukkit.configuration.file.FileConfiguration;
import org.buktify.configurate.serialization.serializer.Serializer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class ListSerializer implements Serializer<List<?>> {

    @Override
    public @NotNull List<?> deserialize(@NotNull String path, @NotNull FileConfiguration configuration) {
        return Objects.requireNonNull(configuration.getList(path));
    }
}
