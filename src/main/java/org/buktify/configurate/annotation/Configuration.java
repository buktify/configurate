package org.buktify.configurate.annotation;

import org.buktify.configurate.serialization.provider.SerializationProvider;
import org.buktify.configurate.serialization.provider.impl.DummySerializationProvider;
import org.buktify.configurate.serialization.serializer.SerializationType;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code Configuration} annotation is used to mark annotated class as configuration
 * and specify the filename and path for processing and generating configuration file.
 *
 * <p>The annotation can be applied to any class declaration.
 * <p> Example:
 * <pre>{@code
 * @Configuration(
 *         fileName = "test-config.yml",
 *         filePath = "%plugin_root%/someDirectory/%filename%"
 * )
 * public class Settings {
 *
 * }
 * }</pre>
 * <p>The %plugin_root% and %filename% placeholders are required.
 *
 * @since 0.1dev
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Configuration {

    /**
     * @return name of the configuration file.
     */
    @NotNull String fileName();

    /**
     * @return path of the configuration file.
     */
    @NotNull String filePath();

    /**
     * @return configuration's serialization way.
     */
    @NotNull SerializationType serialization() default SerializationType.REFLECT;

    /**
     * @return configuration class plain serializer, if needed.
     */
    @NotNull Class<? extends SerializationProvider<?>> provider() default DummySerializationProvider.class;

}
