package me.makecode.platano.core.command.api.exception;

public final class MissingProviderException extends Exception {
    public MissingProviderException(String message) {
        super(message);
    }

    public MissingProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
