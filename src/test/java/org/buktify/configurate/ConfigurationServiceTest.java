package org.buktify.configurate;

import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.buktify.configurate.annotation.Configuration;
import org.buktify.configurate.annotation.Variable;
import org.buktify.configurate.exception.ConfigurationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
    void whenGivenValidConfiguration_ThenProcess() {
        assertDoesNotThrow(() -> {
            configurationService
                    .rootDirectory(new File("/test"))
                    .registerConfigurations(ValidTestConfiguration.class)
                    .apply();
        });
    }

    @Configuration(
            fileName = "test-config.yml",
            filePath = "%plugin_root%/%filename%"
    )
    @NoArgsConstructor
    @SuppressWarnings({"FieldMayBeFinal", "unused"})
    private static class ValidTestConfiguration {

        @Variable("test.list")
        private List<String> stringList = new ArrayList<>();

        @Variable("test.list-3")
        private List<Material> materials = List.of(Material.AIR, Material.COAL);

    }

    @NoArgsConstructor
    @SuppressWarnings("FieldMayBeFinal")
    private static class InvalidTestConfiguration {

    }
}