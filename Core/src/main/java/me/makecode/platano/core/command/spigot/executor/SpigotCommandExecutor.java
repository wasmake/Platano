package me.makecode.platano.core.command.spigot.executor;

import me.makecode.platano.core.command.spigot.SpigotCommandService;
import me.makecode.platano.core.command.spigot.container.SpigotCommandContainer;
import me.makecode.platano.core.command.spigot.sender.SpigotCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

public final class SpigotCommandExecutor implements CommandExecutor {
    private final SpigotCommandService commandService;
    private final SpigotCommandContainer container;

    public SpigotCommandExecutor(@Nonnull SpigotCommandService commandService, @Nonnull SpigotCommandContainer container) {
        this.commandService = commandService;
        this.container = container;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(container.getName())) {
            SpigotCommandSender spigotCommandSender = new SpigotCommandSender(sender);
            return commandService.executeCommand(spigotCommandSender, container, label, args);
        }
        return false;
    }
}
