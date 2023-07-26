package org.buktify.configurate.serialization.serializer.impl.bukkit;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.buktify.configurate.bukkit.configuration.file.FileConfiguration;
import org.buktify.configurate.serialization.serializer.Serializer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings("unused")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class LocationSerializer implements Serializer<Location> {

    @Override
    public @NotNull Location deserialize(@NotNull String path, @NotNull FileConfiguration configuration) {
        World world = Bukkit.getWorld(Objects.requireNonNull(configuration.getString(path + ".world")));
        double x = configuration.getDouble(path + ".x");
        double y = configuration.getDouble(path + ".y");
        double z = configuration.getDouble(path + ".z");
        float yaw = (float) configuration.getDouble(path + ".yaw");
        float pitch = (float) configuration.getDouble(path + ".pitch");
        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public void serialize(@NotNull Location location, @NotNull String path, @NotNull FileConfiguration configuration) {
        configuration.set(path + ".world", location.getWorld().getName());
        configuration.set(path + ".x", location.getX());
        configuration.set(path + ".y", location.getY());
        configuration.set(path + ".z", location.getZ());
        configuration.set(path + ".yaw", location.getYaw());
        configuration.set(path + ".pitch", location.getPitch());
    }
}
