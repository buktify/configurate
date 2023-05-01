package org.buktify.configurate;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.buktify.configurate.exception.ConfigurationException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ConfigurationPool {

    HashMap<Class<?>, Object> proceedConfigurations = new HashMap<>();

    public void put(@NotNull Object configuration) throws ConfigurationException {
        if (proceedConfigurations.containsKey(configuration.getClass()))
            throw new ConfigurationException("Can't put  " + configuration.getClass().getSimpleName() + " to configuration pool. Configuration already proceed.");
        proceedConfigurations.put(configuration.getClass(), configuration);
    }

    @SuppressWarnings({"unchecked", "unused"})
    public <T> T get(@NotNull Class<T> configurationClass) throws ConfigurationException {
        if (!proceedConfigurations.containsKey(configurationClass))
            throw new ConfigurationException("Can't get  " + configurationClass.getSimpleName() + " from configuration pool. Configuration is not proceed.");
        return (T) proceedConfigurations.get(configurationClass);
    }
}
