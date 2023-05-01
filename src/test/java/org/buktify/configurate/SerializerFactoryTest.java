package org.buktify.configurate;

import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.buktify.configurate.exception.SerializationException;
import org.buktify.configurate.serialization.SerializerFactory;
import org.buktify.configurate.serialization.serializer.Serializer;
import org.buktify.configurate.serialization.serializer.impl.StringSerializer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
        assertEquals(getSerializer.invoke(serializerFactory, String.class).getClass(), StringSerializer.class);
    }

    @Test
    @SneakyThrows
    void whenClassGiven_AndSerializerNotRegistered_ThenThrowException() {
        Method getSerializer = SerializerFactory.class.getDeclaredMethod("getSerializer", Class.class);
        getSerializer.setAccessible(true);
        assertThrows(InvocationTargetException.class, () -> getSerializer.invoke(serializerFactory, Field.class));
    }

    @Test
    void whenGivenClassNotImplementingSerializer_ThenThrowException() {
        assertThrows(SerializationException.class, () -> serializerFactory.register(String.class));
    }

    @Test
    void whenGivenClassWithIncorrectNaming_ThenThrowException() {
        assertThrows(SerializationException.class, () -> serializerFactory.register(SerializerString.class));
    }

    @Test
    void whenGivenSerializer_AndAlreadyRegistered_ThenThrowException() {
        assertThrows(SerializationException.class, () -> serializerFactory.register(StringSerializer.class));
    }

    private static class SerializerString implements Serializer<String> {

        @Override
        public String deserialize(@NotNull String path, @NotNull FileConfiguration configuration) {
            return null;
        }
    }
}
