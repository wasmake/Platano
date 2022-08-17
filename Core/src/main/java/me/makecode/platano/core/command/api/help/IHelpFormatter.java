package me.makecode.platano.core.command.api.help;

import me.makecode.platano.core.command.api.command.CommandContainer;
import me.makecode.platano.core.command.api.sender.ICommandSender;

import javax.annotation.Nonnull;

public interface IHelpFormatter {
    void sendHelpFor(@Nonnull ICommandSender<?> sender, @Nonnull CommandContainer container);
}
