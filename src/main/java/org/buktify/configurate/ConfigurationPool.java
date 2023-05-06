package org.buktify.configurate;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.buktify.configurate.exception.ConfigurationException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * ConfigurationPool is a class that manages a pool of configuration objects.
 * It ensures that only one instance of each
 * configuration object can be added to the pool, preventing duplicate configurations.
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ConfigurationPool {

    HashMap<Class<?>, Object> proceedConfigurations = new HashMap<>();

    /**
     * Adds a configuration object to the pool. If the pool already contains an instance of the configuration object's
     * class, a ConfigurationException is thrown to prevent duplicate configurations.
     *
     * @param configuration The configuration object to add to the pool.
     * @throws ConfigurationException If the configuration object's class is already in the pool.
     */
    public void put(@NotNull Object configuration) throws ConfigurationException {
        if (proceedConfigurations.containsKey(configuration.getClass()))
            throw new ConfigurationException("Can't put  " + configuration.getClass().getSimpleName() + " to configuration pool. Configuration already proceed.");
        proceedConfigurations.put(configuration.getClass(), configuration);
    }

    /**
     * Retrieves a configuration object from the pool. If the requested configuration object is not in the pool, a
     * ConfigurationException is thrown.
     *
     * @param configurationClass The class of the configuration object to retrieve from the pool.
     * @param <T>                The type of the configuration object to retrieve from the pool.
     * @return The configuration object of the specified class.
     * @throws ConfigurationException If the requested configuration object is not in the pool.
     */
    @SuppressWarnings({"unchecked", "unused"})
    public <T> T get(@NotNull Class<T> configurationClass) throws ConfigurationException {
        if (!proceedConfigurations.containsKey(configurationClass))
            throw new ConfigurationException("Can't get  " + configurationClass.getSimpleName() + " from configuration pool. Configuration is not proceed.");
        return (T) proceedConfigurations.get(configurationClass);
    }
}
