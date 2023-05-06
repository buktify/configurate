package org.buktify.configurate;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.buktify.configurate.annotation.Configuration;
import org.buktify.configurate.exception.ConfigurationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigurationServiceTest {

    private ConfigurationService configurationService;

    @BeforeEach
    void setUp() {
        configurationService = new ConfigurationService();
    }

    @Test
    void whenRootDirectoryIsNull_ThenThrowException() {
        assertThrows(ConfigurationException.class, () -> configurationService.apply());
    }

    @Test
    void whenGivenInvalidConfiguration_ThenThrowException() {
        configurationService
                .rootDirectory(new File(""))
                .registerConfigurations(InvalidTestConfiguration.class);
        assertThrows(ConfigurationException.class, () -> configurationService.apply());
    }

    @Test
    @SneakyThrows(ConfigurationException.class)
    void whenGivenValidConfiguration_ThenPutToConfigurationPool() {
        configurationService
                .rootDirectory(new File(""))
                .registerConfigurations(ValidTestConfiguration.class)
                .apply();
        assertNotNull(configurationService.getConfigurationPool().get(ValidTestConfiguration.class));
    }

    @Configuration(
            fileName = "test-config.yml",
            filePath = "%plugin_root%/someDirectory/%filename%"
    )
    @NoArgsConstructor
    @SuppressWarnings({"FieldMayBeFinal", "unused"})
    private static class ValidTestConfiguration {

        private List<String> stringList = new ArrayList<>();

    }

    @NoArgsConstructor
    @SuppressWarnings("FieldMayBeFinal")
    private static class InvalidTestConfiguration {

    }
}