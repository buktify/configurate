package org.buktify.configurate.serialization.serializer.impl;

import org.bukkit.configuration.file.FileConfiguration;
import org.buktify.configurate.serialization.serializer.Serializer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class TypedListSerializer {

    public <T> List<T> deserialize(@NotNull Serializer<T> typeSerializer, @NotNull String path, @NotNull FileConfiguration configuration) {
        List<T> list = new ArrayList<>();
        Objects.requireNonNull(configuration.getConfigurationSection(path)).getKeys(false).forEach(key -> list.add(typeSerializer.deserialize(path + "." + key, configuration)));
        return list;
    }

    public <T> void serialize(@NotNull List<T> list, @NotNull Serializer<T> typeSerializer, @NotNull String path, @NotNull FileConfiguration configuration) {
        for (int i = 0; i < list.size(); i++) {
            typeSerializer.serialize(list.get(i), path + "." + i, configuration);
        }
    }
}
