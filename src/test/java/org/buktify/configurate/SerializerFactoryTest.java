package org.buktify.configurate;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.buktify.configurate.annotation.Configuration;
import org.buktify.configurate.annotation.Variable;
import org.buktify.configurate.exception.ConfigurationException;
import org.buktify.configurate.serialization.serializer.Serializer;
import org.buktify.configurate.serialization.serializer.SerializerFactory;
import org.buktify.configurate.serialization.serializer.SerializerFactoryImpl;
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
        serializerFactory = new SerializerFactoryImpl();
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
    void whenClassGiven_AndSerializerNotRegistered_ThenThrowException() {
        Method getSerializer = SerializerFactory.class.getDeclaredMethod("getSerializer", Class.class);
        getSerializer.setAccessible(true);
        assertThrows(InvocationTargetException.class, () -> getSerializer.invoke(serializerFactory, Field.class));
    }


    @Test
    @SuppressWarnings("all")
    void whenGivenClassWithIncorrectNaming_ThenThrowException() {
        assertThrows(ConfigurationException.class, () -> serializerFactory.registerSerializers(SerializerString.class));
    }

    @Test
    @SuppressWarnings("all")
    void whenGivenSerializer_AndAlreadyRegistered_ThenThrowException() {
        assertThrows(ConfigurationException.class, () -> serializerFactory.registerSerializers(StringSerializer.class));
    }

    @Configuration(
            fileName = "test-config.yml",
            filePath = "%plugin_root%/someDirectory/%filename%"
    )
    @NoArgsConstructor
    @SuppressWarnings({"FieldMayBeFinal", "unused"})
    private static class ValidTestConfiguration {

        @Variable(value = "mob", serializer = StringSerializer.class)
        Mob mob;
        private List<Mob> stringList = new ArrayList<>();
        private List<List<List<Material>>> someOtherList = new ArrayList<>();

    }

    private static class Mob {

    }

    private static class SerializerString implements Serializer<String> {

        @Override
        public @NotNull String deserialize(@NotNull String path, @NotNull FileConfiguration configuration) {
            return null;
        }
    }
}
