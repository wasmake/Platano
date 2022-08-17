package me.makecode.platano.core.command.bungee.authorizer;

import me.makecode.platano.core.command.api.authorizer.IAuthorizer;
import me.makecode.platano.core.command.api.command.WrappedCommand;
import me.makecode.platano.core.command.api.sender.ICommandSender;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

public final class BungeeAuthorizer implements IAuthorizer<CommandSender> {
    @Override
    public boolean isAuthorized(@Nonnull ICommandSender<CommandSender> sender, @Nonnull WrappedCommand command) {
        if (command.getPermission() != null && command.getPermission().length() > 0) {
            if (!sender.hasPermission(command.getPermission())) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to perform this command.");
                return false;
            }
        }
        return true;
    }
}
