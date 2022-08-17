package me.makecode.platano.core.command.bungee.provider;

import me.makecode.platano.core.command.api.argument.CommandArg;
import me.makecode.platano.core.command.api.exception.CommandExitMessage;
import me.makecode.platano.core.command.api.parametric.CommandProvider;
import me.makecode.platano.core.command.bungee.BungeeCommandService;
import me.makecode.platano.core.command.bungee.lang.BungeeLang;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

public final class PlayerSenderProvider extends CommandProvider<ProxiedPlayer> {
    private final BungeeCommandService service;

    public PlayerSenderProvider(@Nonnull BungeeCommandService service) {
        this.service = service;
    }

    @Override
    public boolean doesConsumeArgument() {
        return false;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    @Nullable
    public ProxiedPlayer provide(@Nonnull CommandArg arg, @Nonnull List<? extends Annotation> annotations) throws CommandExitMessage {
        if (arg.getSender().getInstance() instanceof ProxiedPlayer) {
            return (ProxiedPlayer) arg.getSender().getInstance();
        }
        throw new CommandExitMessage(service.getLang().get(BungeeLang.Type.PLAYER_ONLY_COMMAND));
    }

    @Override
    public String argumentDescription() {
        return "player sender";
    }

    @Override
    public List<String> getSuggestions(@Nonnull String prefix) {
        return Collections.emptyList();
    }
}