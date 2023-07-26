package org.buktify.configurate.serialization.serializer.impl.provider.impl;

import org.buktify.configurate.bukkit.configuration.file.FileConfiguration;
import org.buktify.configurate.serialization.serializer.impl.provider.SerializationProvider;
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
