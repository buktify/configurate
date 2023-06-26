package org.buktify.configurate.serialization.serializer.impl.bukkit;

import org.bukkit.configuration.file.FileConfiguration;
import org.buktify.configurate.serialization.serializer.Serializer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

@SuppressWarnings("unused")
public class HashMapSerializer implements Serializer<HashMap<?, ?>> {

    @Override
    public HashMap<?, ?> deserialize(@NotNull String path, @NotNull FileConfiguration configuration) {
        return (HashMap<?, ?>) Objects.requireNonNull(configuration.getConfigurationSection(path)).getValues(false);
    }
}
