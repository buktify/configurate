package org.buktify.configurate.serialization.serializer.impl.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.buktify.configurate.serialization.serializer.Serializer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings("unused")
public class WorldSerializer implements Serializer<World> {

    @Override
    public @NotNull World deserialize(@NotNull String path, @NotNull FileConfiguration configuration) {
        return Objects.requireNonNull(Bukkit.getWorld(Objects.requireNonNull(configuration.getString(path))));
    }

    @Override
    public void serialize(@NotNull World world, @NotNull String path, @NotNull FileConfiguration configuration) {
        configuration.set(path, world.getName());
    }
}
