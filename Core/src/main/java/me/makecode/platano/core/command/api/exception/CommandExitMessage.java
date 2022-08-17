package me.makecode.platano.core.command.api.exception;

public final class CommandExitMessage extends Exception {
    public CommandExitMessage(String message) {
        super(message);
    }
}
