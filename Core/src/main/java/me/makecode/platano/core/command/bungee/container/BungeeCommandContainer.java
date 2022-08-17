package me.makecode.platano.core.command.bungee.container;

import me.makecode.platano.core.command.api.command.AbstractCommandService;
import me.makecode.platano.core.command.api.command.CommandContainer;
import me.makecode.platano.core.command.api.command.WrappedCommand;
import me.makecode.platano.core.command.bungee.executor.BungeeCommandExecutor;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.TabExecutor;

import me.makecode.platano.core.command.bungee.BungeeCommandService;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class BungeeCommandContainer extends CommandContainer {
    public BungeeCommandContainer(AbstractCommandService<?> commandService, Object object, String name, Set<String> aliases, Map<String, WrappedCommand> commands) {
        super(commandService, object, name, aliases, commands);
    }

    public final class BungeeCommand extends Command implements TabExecutor {
        private final BungeeCommandService commandService;
        private final BungeeCommandExecutor commandExecutor;

        public BungeeCommand(@Nonnull BungeeCommandService commandService) {
            super(name, "", aliases.toArray(new String[aliases.size()]));
            this.commandService = commandService;
            commandExecutor = new BungeeCommandExecutor(commandService, BungeeCommandContainer.this);
        }

        @Override
        public void execute(CommandSender commandSender, String[] strings) {
            commandExecutor.onCommand(commandSender, this, name, strings);
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, String[] args) throws IllegalArgumentException {
            return tabCompleter.onTabComplete(getName(), args);
        }

    }
}
