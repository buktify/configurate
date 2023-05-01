package org.buktify.configurate;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.buktify.configurate.annotation.Configuration;
import org.buktify.configurate.exception.ConfigurationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationServiceTest {

    private ConfigurationService configurationService;

    @BeforeEach
    void setUp() {
        configurationService = new ConfigurationService();
    }

    @Test
    @SneakyThrows
    void givenStringList_ThenReturnStringClass() {
        Method getGenericType = ConfigurationService.class.getDeclaredMethod("getGenericType", Field.class);
        getGenericType.setAccessible(true);
        Class<?> result = (Class<?>) getGenericType.invoke(configurationService, ValidTestConfiguration.class.getDeclaredField("stringList"));
        assertEquals(result, String.class);
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
    @SuppressWarnings("FieldMayBeFinal")
    private static class ValidTestConfiguration {

        private List<String> stringList = new ArrayList<>();

    }

    @NoArgsConstructor
    @SuppressWarnings("FieldMayBeFinal")
    private static class InvalidTestConfiguration {

    }
}