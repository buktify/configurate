package org.buktify.configurate.serialization.serializer.impl;

import org.bukkit.configuration.file.FileConfiguration;
import org.buktify.configurate.serialization.serializer.Serializer;
import org.jetbrains.annotations.NotNull;

@Deprecated
@SuppressWarnings("unused")
public class AdaptiveSerializer implements Serializer<Void> {

    @Override
    public @NotNull Void deserialize(@NotNull String path, @NotNull FileConfiguration configuration) {
        return null;
    }
}
