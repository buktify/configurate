package org.buktify.configurate.annotation;

import org.buktify.configurate.serialization.serializer.Serializer;
import org.buktify.configurate.serialization.serializer.impl.AdaptiveSerializer;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code Variable} annotation is used to indicate that a field represents a variable,
 * connected to configuration property.
 *
 * <p>The annotation can be applied to any field declaration.
 *
 * @since 0.1dev
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@SuppressWarnings("deprecation")
public @interface Variable {

    /**
     * @return path to property in configuration file
     */
    @NotNull String value();

    /**
     * @return specified serializer.
     */
    @NotNull Class<? extends Serializer<?>> serializer() default AdaptiveSerializer.class;
}
