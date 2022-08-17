package me.makecode.platano.command;

import lombok.Getter;
import me.makecode.platano.core.command.spigot.SpigotCommandService;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DrinkManager {
    private List<DrinkAbstractcommand> commands;
    private SpigotCommandService commandService;

    public DrinkManager(JavaPlugin plugin){
        commandService = new SpigotCommandService(plugin);
        commands = new ArrayList<>();
    }

    public void getClassCommands(){
        registerCommands();
    }

    public void registerCommands(){
        commands.forEach(drinkCommand -> {
            commandService.register(drinkCommand, drinkCommand.getName(), drinkCommand.getAliases()).setOverrideExistingCommands(false);
        });
        commandService.registerCommands();
    }

}
