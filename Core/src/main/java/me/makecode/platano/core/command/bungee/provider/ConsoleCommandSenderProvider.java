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

public final class ConsoleCommandSenderProvider extends CommandProvider<ConsoleCommandSender> {
    private final BungeeCommandService service;

    public ConsoleCommandSenderProvider(@Nonnull BungeeCommandService service) {
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

    @Nullable
    @Override
    public ConsoleCommandSender provide(@Nonnull CommandArg arg, @Nonnull List<? extends Annotation> annotations) throws CommandExitMessage {
        if (arg.getSender().getInstance() instanceof ProxiedPlayer) {
            throw new CommandExitMessage(service.getLang().get(BungeeLang.Type.CONSOLE_ONLY_COMMAND));
        }
        return (ConsoleCommandSender) arg.getSender().getInstance();
    }

    @Override
    public String argumentDescription() {
        return "console sender";
    }

    @Override
    public List<String> getSuggestions(@Nonnull String prefix) {
        return Collections.emptyList();
    }
}