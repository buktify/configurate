package org.buktify.configurate.serialization.serializer.impl.bukkit;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.buktify.configurate.serialization.serializer.Serializer;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ItemStackSerializer implements Serializer<ItemStack> {

    @Override
    public ItemStack deserialize(@NotNull String path, @NotNull FileConfiguration configuration) {
        return configuration.getItemStack(path);
    }

    @Override
    public void serialize(@NotNull ItemStack itemStack, @NotNull String path, @NotNull FileConfiguration configuration) {
        configuration.set(path, configuration);
    }
}
