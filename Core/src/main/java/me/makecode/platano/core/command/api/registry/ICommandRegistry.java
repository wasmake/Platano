package me.makecode.platano.core.command.api.registry;

import me.makecode.platano.core.command.api.command.CommandContainer;
import me.makecode.platano.core.command.api.exception.CommandRegistrationException;

import javax.annotation.Nonnull;

public interface ICommandRegistry<T extends CommandContainer> {
    boolean register(@Nonnull T container, boolean unregisterExisting) throws CommandRegistrationException;
}
