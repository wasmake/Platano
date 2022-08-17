package me.makecode.platano.core.command.bungee.provider;

import me.makecode.platano.core.command.api.argument.CommandArg;
import me.makecode.platano.core.command.api.exception.CommandExitMessage;
import me.makecode.platano.core.command.api.parametric.CommandProvider;
import me.makecode.platano.core.command.bungee.BungeeCommandService;
import me.makecode.platano.core.command.bungee.lang.BungeeLang;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;

public final class PlayerProvider extends CommandProvider<ProxiedPlayer> {
    private final BungeeCommandService service;

    public PlayerProvider(@Nonnull BungeeCommandService service) {
        this.service = service;
    }

    @Override
    public boolean doesConsumeArgument() {
        return true;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Nonnull
    @Override
    public ProxiedPlayer provide(@Nonnull CommandArg arg, @Nonnull List<? extends Annotation> annotations) throws CommandExitMessage {
        String name = arg.get();
        ProxiedPlayer p = ProxyServer.getInstance().getPlayer(name);
        if (p != null) {
            return p;
        }
        throw new CommandExitMessage(service.getLang().get(BungeeLang.Type.PLAYER_NOT_FOUND, name));
    }

    @Override
    public String argumentDescription() {
        return "player";
    }

    @Override
    public List<String> getSuggestions(@Nonnull String prefix) {
        return ProxyServer.getInstance().getPlayers()
                .stream()
                .map(player -> player.getName().toLowerCase())
                .filter(name -> prefix.length() == 0 || name.startsWith(prefix.toLowerCase()))
                .collect(Collectors.toList());
    }
}
