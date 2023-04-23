package dev.temez.configurate.exception;

import org.jetbrains.annotations.NotNull;

/**
 * Thrown when some problems with configuration appeared
 */
public class ConfigurationException extends Exception {
    public ConfigurationException(@NotNull String message) {
        super(message);
    }
}
