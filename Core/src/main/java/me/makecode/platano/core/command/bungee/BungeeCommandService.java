package me.makecode.platano.core.command.bungee;

import me.makecode.platano.core.command.api.annotation.Sender;
import me.makecode.platano.core.command.api.authorizer.IAuthorizer;
import me.makecode.platano.core.command.api.command.AbstractCommandService;
import me.makecode.platano.core.command.api.command.WrappedCommand;
import me.makecode.platano.core.command.bungee.authorizer.BungeeAuthorizer;
import me.makecode.platano.core.command.bungee.container.BungeeCommandContainer;
import me.makecode.platano.core.command.bungee.lang.BungeeLang;
import me.makecode.platano.core.command.bungee.provider.*;
import me.makecode.platano.core.command.bungee.registry.BungeeCommandRegistry;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;

public final class BungeeCommandService extends AbstractCommandService<BungeeCommandContainer> {
    private final Plugin plugin;
    private final BungeeCommandRegistry registry = new BungeeCommandRegistry(this);
    private final BungeeLang bungeeLang = new BungeeLang();

    public BungeeCommandService(@Nonnull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void runAsync(@Nonnull Runnable runnable) {
        plugin.getProxy().getScheduler().runAsync(plugin, runnable);
    }

    @Override
    protected void bindDefaults() {
        bind(CommandSender.class).annotatedWith(Sender.class).toProvider(new CommandSenderProvider());
        bind(ProxiedPlayer.class).annotatedWith(Sender.class).toProvider(new PlayerSenderProvider(this));
        bind(ConsoleCommandSender.class).annotatedWith(Sender.class).toProvider(new ConsoleCommandSenderProvider(this));

        bind(ProxiedPlayer.class).toProvider(new PlayerProvider(this));
    }

    @Override
    protected IAuthorizer<?> getDefaultAuthorizer() {
        return new BungeeAuthorizer();
    }

    @Nonnull
    @Override
    public BungeeCommandContainer createContainer(@Nonnull AbstractCommandService<?> commandService, @Nonnull Object object, @Nonnull String name, @Nonnull Set<String> aliases, @Nonnull Map<String, WrappedCommand> commands) {
        return new BungeeCommandContainer(commandService, object, name, aliases, commands);
    }

    @Override
    public void registerCommands() {
        for (BungeeCommandContainer value : commands.values()) {
            registry.register(value, true);
        }
    }

    @Override
    public BungeeLang getLang() {
        return bungeeLang;
    }

    @Nonnull
    public Plugin getPlugin() {
        return plugin;
    }
}
