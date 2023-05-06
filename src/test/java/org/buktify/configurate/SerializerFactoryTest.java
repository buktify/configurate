package org.buktify.configurate;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.buktify.configurate.annotation.Configuration;
import org.buktify.configurate.exception.SerializationException;
import org.buktify.configurate.serialization.SerializerFactory;
import org.buktify.configurate.serialization.serializer.Serializer;
import org.buktify.configurate.serialization.serializer.impl.ListSerializer;
import org.buktify.configurate.serialization.serializer.impl.StringSerializer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class SerializerFactoryTest {

    private SerializerFactory serializerFactory;

    @BeforeEach
    void setUp() {
        serializerFactory = new SerializerFactory();
    }

    @Test
    @SneakyThrows
    void whenClassGiven_ThenReturnRegisteredSerializer() {
        Method getSerializer = SerializerFactory.class.getDeclaredMethod("getSerializer", Class.class);
        getSerializer.setAccessible(true);
        assertEquals(getSerializer.invoke(serializerFactory, List.class).getClass(), ListSerializer.class);
    }

    @Test
    @SneakyThrows
    void givenStringList_ThenReturnStringClass() {
        Method getGenericType = SerializerFactory.class.getDeclaredMethod("getFieldGenericType", Field.class);
        getGenericType.setAccessible(true);
        Class<?> result = (Class<?>) getGenericType.invoke(serializerFactory, SerializerFactoryTest.ValidTestConfiguration.class.getDeclaredField("stringList"));
        assertEquals(result, String.class);
    }

    @Test
    @SneakyThrows
    void whenClassGiven_AndSerializerNotRegistered_ThenThrowException() {
        Method getSerializer = SerializerFactory.class.getDeclaredMethod("getSerializer", Class.class);
        getSerializer.setAccessible(true);
        assertThrows(InvocationTargetException.class, () -> getSerializer.invoke(serializerFactory, Field.class));
    }

    @Test
    void whenGivenClassWithIncorrectNaming_ThenThrowException() {
        assertThrows(SerializationException.class, () -> serializerFactory.register(SerializerString.class));
    }

    @Test
    void whenGivenSerializer_AndAlreadyRegistered_ThenThrowException() {
        assertThrows(SerializationException.class, () -> serializerFactory.register(StringSerializer.class));
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

    private static class SerializerString implements Serializer<String> {

        @Override
        public String deserialize(@NotNull String path, @NotNull FileConfiguration configuration) {
            return null;
        }
    }
}
