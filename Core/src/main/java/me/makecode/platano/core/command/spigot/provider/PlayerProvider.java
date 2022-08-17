package me.makecode.platano.core.command.spigot.provider;

import me.makecode.platano.core.command.api.argument.CommandArg;
import me.makecode.platano.core.command.api.exception.CommandExitMessage;
import me.makecode.platano.core.command.api.parametric.CommandProvider;
import me.makecode.platano.core.command.spigot.SpigotCommandService;
import me.makecode.platano.core.command.spigot.lang.SpigotLang;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;

public final class PlayerProvider extends CommandProvider<Player> {
    private final SpigotCommandService service;

    public PlayerProvider(@Nonnull SpigotCommandService service) {
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
    public Player provide(@Nonnull CommandArg arg, @Nonnull List<? extends Annotation> annotations) throws CommandExitMessage {
        String name = arg.get();
        Player p = Bukkit.getServer().getPlayer(name);
        if (p != null) {
            return p;
        }
        throw new CommandExitMessage(service.getLang().get(SpigotLang.Type.PLAYER_NOT_FOUND, name));
    }

    @Override
    public String argumentDescription() {
        return "player";
    }

    @Override
    public List<String> getSuggestions(@Nonnull String prefix) {
        return Bukkit.getServer().getOnlinePlayers()
                .stream()
                .map(player -> player.getName().toLowerCase())
                .filter(name -> prefix.length() == 0 || name.startsWith(prefix.toLowerCase()))
                .collect(Collectors.toList());
    }
}
