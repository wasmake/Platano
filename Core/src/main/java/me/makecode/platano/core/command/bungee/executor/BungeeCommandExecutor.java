package me.makecode.platano.core.command.bungee.executor;

import me.makecode.platano.core.command.bungee.BungeeCommandService;
import me.makecode.platano.core.command.bungee.container.BungeeCommandContainer;
import me.makecode.platano.core.command.bungee.sender.BungeeCommandSender;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import javax.annotation.Nonnull;

public final class BungeeCommandExecutor {
    private final BungeeCommandService commandService;
    private final BungeeCommandContainer container;

    public BungeeCommandExecutor(@Nonnull BungeeCommandService commandService, @Nonnull BungeeCommandContainer container) {
        this.commandService = commandService;
        this.container = container;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(container.getName())) {
            BungeeCommandSender bungeeCommandSender = new BungeeCommandSender(sender);
            return commandService.executeCommand(bungeeCommandSender, container, label, args);
        }
        return false;
    }
}
