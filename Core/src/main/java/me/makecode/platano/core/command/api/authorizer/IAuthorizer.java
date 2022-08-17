package me.makecode.platano.core.command.api.authorizer;

import me.makecode.platano.core.command.api.command.WrappedCommand;
import me.makecode.platano.core.command.api.sender.ICommandSender;

import javax.annotation.Nonnull;

public interface IAuthorizer<T> {
    boolean isAuthorized(@Nonnull ICommandSender<T> sender, @Nonnull WrappedCommand command);
}
