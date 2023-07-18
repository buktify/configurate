package org.buktify.configurate.exception;

import org.jetbrains.annotations.NotNull;

/**
 * Thrown when some problems with configuration appeared
 */
public class ConfigurationException extends RuntimeException {
    public ConfigurationException(@NotNull String message) {
        super(message);
    }
}
