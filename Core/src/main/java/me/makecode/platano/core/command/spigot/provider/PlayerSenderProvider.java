package me.makecode.platano.core.command.spigot.provider;

import me.makecode.platano.core.command.api.argument.CommandArg;
import me.makecode.platano.core.command.api.exception.CommandExitMessage;
import me.makecode.platano.core.command.api.parametric.CommandProvider;
import org.bukkit.entity.Player;
import me.makecode.platano.core.command.spigot.SpigotCommandService;
import me.makecode.platano.core.command.spigot.lang.SpigotLang;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

public final class PlayerSenderProvider extends CommandProvider<Player> {
    private final SpigotCommandService service;

    public PlayerSenderProvider(@Nonnull SpigotCommandService service) {
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
    public Player provide(@Nonnull CommandArg arg, @Nonnull List<? extends Annotation> annotations) throws CommandExitMessage {
        if (arg.getSender().getInstance() instanceof Player) {
            return (Player) arg.getSender().getInstance();
        }
        throw new CommandExitMessage(service.getLang().get(SpigotLang.Type.PLAYER_ONLY_COMMAND));
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