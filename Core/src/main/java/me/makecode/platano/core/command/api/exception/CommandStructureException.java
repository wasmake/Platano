package me.makecode.platano.core.command.api.exception;

public final class CommandStructureException extends Exception {
    public CommandStructureException() {
    }

    public CommandStructureException(String message) {
        super(message);
    }

    public CommandStructureException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandStructureException(Throwable cause) {
        super(cause);
    }

    public CommandStructureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
