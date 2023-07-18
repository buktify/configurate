package org.buktify.configurate.serialization.provider.impl;

import org.bukkit.configuration.file.FileConfiguration;
import org.buktify.configurate.serialization.provider.SerializationProvider;
import org.jetbrains.annotations.NotNull;

public class DummySerializationProvider implements SerializationProvider<Object> {

    @Override
    public @NotNull Object deserialize(@NotNull FileConfiguration configuration) {
        return null;
    }

    @Override
    public void serialize(@NotNull Object object, @NotNull FileConfiguration configuration) {

    }
}
