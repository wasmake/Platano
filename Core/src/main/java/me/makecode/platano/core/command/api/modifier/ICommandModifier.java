package me.makecode.platano.core.command.api.modifier;

import me.makecode.platano.core.command.api.parametric.CommandParameter;
import me.makecode.platano.core.command.api.command.CommandExecution;
import me.makecode.platano.core.command.api.exception.CommandExitMessage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public interface ICommandModifier<T> {
    Optional<T> modify(@Nonnull CommandExecution execution, @Nonnull CommandParameter commandParameter, @Nullable T argument) throws CommandExitMessage;
}
