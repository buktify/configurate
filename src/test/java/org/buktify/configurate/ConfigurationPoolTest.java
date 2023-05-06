package org.buktify.configurate;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.buktify.configurate.annotation.Configuration;
import org.buktify.configurate.exception.ConfigurationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigurationPoolTest {

    private ConfigurationPool configurationPool;

    @BeforeEach
    void setUp() {
        configurationPool = new ConfigurationPool();
    }

    @Test
    void whenConfigurationNotLoaded_ThenThrowException() {
        assertThrows(ConfigurationException.class, () -> configurationPool.get(ValidTestConfiguration.class));
    }

    @Test
    @SneakyThrows
    void whenConfigurationLoaded_ThenReturn() {
        configurationPool.put(new ValidTestConfiguration());
        assertNotNull(configurationPool.get(ValidTestConfiguration.class));
    }

    @Test
    @SneakyThrows
    void whenConfigurationAlreadyLoaded_ThenThrowException() {
        configurationPool.put(new ValidTestConfiguration());
        assertThrows(ConfigurationException.class, () -> configurationPool.put(new ValidTestConfiguration()));
    }

    @Configuration(
            fileName = "test-config.yml",
            filePath = "%plugin_root%/someDirectory/%filename%"
    )
    @NoArgsConstructor
    @SuppressWarnings("FieldMayBeFinal")
    private static class ValidTestConfiguration {

    }
}