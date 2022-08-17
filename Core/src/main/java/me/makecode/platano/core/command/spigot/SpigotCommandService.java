package me.makecode.platano.core.command.spigot;

import me.makecode.platano.core.command.api.annotation.Sender;
import me.makecode.platano.core.command.api.authorizer.IAuthorizer;
import me.makecode.platano.core.command.api.command.AbstractCommandService;
import me.makecode.platano.core.command.api.command.WrappedCommand;
import me.makecode.platano.core.command.spigot.authorizer.SpigotAuthorizer;
import me.makecode.platano.core.command.spigot.container.SpigotCommandContainer;
import me.makecode.platano.core.command.spigot.lang.SpigotLang;
import me.makecode.platano.core.command.spigot.provider.PlayerSenderProvider;
import me.makecode.platano.core.command.spigot.registry.SpigotCommandRegistry;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import me.makecode.platano.core.command.spigot.provider.CommandSenderProvider;
import me.makecode.platano.core.command.spigot.provider.ConsoleCommandSenderProvider;
import me.makecode.platano.core.command.spigot.provider.PlayerProvider;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;

public final class SpigotCommandService extends AbstractCommandService<SpigotCommandContainer> {
    private final JavaPlugin plugin;
    private final SpigotCommandRegistry registry = new SpigotCommandRegistry(this);
    private final SpigotLang spigotLang = new SpigotLang();

    public SpigotCommandService(@Nonnull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void runAsync(@Nonnull Runnable runnable) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    @Override
    protected void bindDefaults() {
        bind(CommandSender.class).annotatedWith(Sender.class).toProvider(new CommandSenderProvider());
        bind(Player.class).annotatedWith(Sender.class).toProvider(new PlayerSenderProvider(this));
        bind(ConsoleCommandSender.class).annotatedWith(Sender.class).toProvider(new ConsoleCommandSenderProvider(this));

        bind(Player.class).toProvider(new PlayerProvider(this));
    }

    @Override
    protected IAuthorizer<?> getDefaultAuthorizer() {
        return new SpigotAuthorizer();
    }

    @Nonnull
    @Override
    public SpigotCommandContainer createContainer(@Nonnull AbstractCommandService<?> commandService, @Nonnull Object object, @Nonnull String name, @Nonnull Set<String> aliases, @Nonnull Map<String, WrappedCommand> commands) {
        return new SpigotCommandContainer(commandService, object, name, aliases, commands);
    }

    @Override
    public void registerCommands() {
        for (SpigotCommandContainer value : commands.values()) {
            registry.register(value, true);
        }
    }

    @Override
    public SpigotLang getLang() {
        return spigotLang;
    }

    @Nonnull
    public JavaPlugin getPlugin() {
        return plugin;
    }
}
