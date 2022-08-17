package me.makecode.platano.core.command.bungee.sender;

import me.makecode.platano.core.command.api.sender.ICommandSender;
import net.md_5.bungee.api.CommandSender;

import javax.annotation.Nonnull;

public final class BungeeCommandSender implements ICommandSender<CommandSender> {
    private final CommandSender commandSender;

    public BungeeCommandSender(@Nonnull CommandSender commandSender) {
        this.commandSender = commandSender;
    }

    @Nonnull
    @Override
    public String getName() {
        return commandSender.getName();
    }

    @Override
    public void sendMessage(@Nonnull String message) {
        commandSender.sendMessage(message);
    }

    @Override
    public boolean hasPermission(@Nonnull String permission) {
        return commandSender.hasPermission(permission);
    }

    @Nonnull
    @Override
    public CommandSender getInstance() {
        return commandSender;
    }
}
