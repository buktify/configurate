package org.buktify.configurate.serialization.serializer.impl.bukkit;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.buktify.configurate.serialization.serializer.Serializer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MaterialSerializer implements Serializer<Material> {
    @Override
    public @NotNull Material deserialize(@NotNull String path, @NotNull FileConfiguration configuration) {
        return Material.valueOf(Objects.requireNonNull(configuration.getString(path)).toUpperCase());
    }

    @Override
    public void serialize(@NotNull Material object, @NotNull String path, @NotNull FileConfiguration configuration) {
        configuration.set(path, object.name().toUpperCase());
    }
}
