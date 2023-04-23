package dev.temez.configurate.annotation;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code Comment} annotation is used to add comments to yaml properties.
 *
 * <p>The annotation can be applied to any field declaration, annotated with {@link Variable}.
 *
 * @since 0.1dev
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Comment {

    /**
     * @return an array of the comments for the annotated field.
     */
    @NotNull String[] value();
}
