package org.buktify.configurate.pool;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.buktify.configurate.exception.ConfigurationException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ConfigurationPoolImpl implements ConfigurationPool {

    HashMap<Class<?>, Object> proceedConfigurations = new HashMap<>();

    /**
     * {@inheritDoc}
     *
     * @param configurationClass The class type of the configuration to retrieve.
     * @param <T>
     * @return
     * @throws ConfigurationException
     */
    @Override
    public <T> @NotNull T getConfiguration(@NotNull Class<T> configurationClass) throws ConfigurationException {
        if (!proceedConfigurations.containsKey(configurationClass))
            throw new ConfigurationException("Can't get  " + configurationClass.getSimpleName() +
                    " from configuration pool. Configuration is not proceed.");
        return configurationClass.cast(proceedConfigurations.get(configurationClass));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() {
        proceedConfigurations.clear();
    }

    /**
     * {@inheritDoc}
     *
     * @param configuredObject The object to be configured.
     * @throws ConfigurationException
     */
    @Override
    public void addConfiguration(@NotNull Object configuredObject) throws ConfigurationException {
        if (proceedConfigurations.containsKey(configuredObject.getClass()))
            throw new ConfigurationException("Can't put  " + configuredObject.getClass().getSimpleName() +
                    " to configuration pool. Configuration already proceed.");
        proceedConfigurations.put(configuredObject.getClass(), configuredObject);
    }
}
