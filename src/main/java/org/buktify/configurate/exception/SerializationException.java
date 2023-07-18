package org.buktify.configurate.exception;

import org.jetbrains.annotations.NotNull;

/**
 * Thrown when some problems with serialization or deserialization appeared
 */
public class SerializationException extends RuntimeException {
    public SerializationException(@NotNull String message) {
        super(message);
    }
}
