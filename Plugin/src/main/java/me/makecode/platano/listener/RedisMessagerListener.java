package me.makecode.platano.listener;

import me.makecode.platano.Kiwii;
import me.makecode.platano.core.event.CoreListener;
import me.makecode.platano.core.event.implement.RedisMessageInEvent;
import me.makecode.platano.core.event.listener.EventHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Pattern;

public class RedisMessagerListener implements CoreListener {

    private final Kiwii kiwii = JavaPlugin.getPlugin(Kiwii.class);

    @EventHandler
    public void getRedisMessagerEvent(RedisMessageInEvent event){
        String message = event.getMessage();
        if(message.contains("$")){
            String[] split = message.split(Pattern.quote("$"));
            String playerName = split[0];
            String command = kiwii.getPlaceholderResolver().substitute(split[1], playerName);

            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            console.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + playerName + " &7command &6" + command));
            kiwii.runSync(() -> Bukkit.dispatchCommand(console, command));
        }
    }

}
