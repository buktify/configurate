package dev.temez.configurate.serialization.serializer.impl;

import dev.temez.configurate.serialization.serializer.Serializer;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListSerializer implements Serializer<List<?>> {

    @Override
    public List<?> deserialize(@NotNull String path, @NotNull FileConfiguration configuration) {
        return configuration.getList(path);
    }

    @Override
    public void serialize(@NotNull List<?> list, @NotNull String path, @NotNull FileConfiguration configuration) {
        Serializer.super.serialize(list, path, configuration);
    }

}
