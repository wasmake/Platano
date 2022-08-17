package me.makecode.platano.core.command.bungee.registry;

import me.makecode.platano.core.command.api.exception.CommandRegistrationException;
import me.makecode.platano.core.command.api.registry.ICommandRegistry;
import me.makecode.platano.core.command.bungee.BungeeCommandService;
import me.makecode.platano.core.command.bungee.container.BungeeCommandContainer;
import net.md_5.bungee.api.ProxyServer;
import org.bukkit.command.CommandMap;

import javax.annotation.Nonnull;

public final class BungeeCommandRegistry implements ICommandRegistry<BungeeCommandContainer> {
    private final BungeeCommandService commandService;
    private CommandMap commandMap;

    public BungeeCommandRegistry(BungeeCommandService commandService) {
        this.commandService = commandService;
    }

    @Override
    public boolean register(@Nonnull BungeeCommandContainer container, boolean unregisterExisting) throws CommandRegistrationException {
        register(container);
        return true;
    }

    public void register(@Nonnull BungeeCommandContainer container){
        ProxyServer.getInstance().getPluginManager().registerCommand(commandService.getPlugin(), container.new BungeeCommand(commandService));
    }
}
