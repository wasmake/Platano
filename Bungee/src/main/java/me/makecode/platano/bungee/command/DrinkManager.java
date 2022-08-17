package me.makecode.platano.bungee.command;

import lombok.Getter;
import me.makecode.platano.bungee.command.admin.StoreCmd;
import me.makecode.platano.core.command.bungee.BungeeCommandService;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DrinkManager {
    private List<DrinkAbstractcommand> commands;
    private BungeeCommandService commandService;

    public DrinkManager(Plugin plugin){
        commandService = new BungeeCommandService(plugin);
        commands = new ArrayList<>();
    }

    public void getClassCommands(){
        new StoreCmd();
        registerCommands();
    }

    public void registerCommands(){
        commands.forEach(drinkCommand -> {
            commandService.register(drinkCommand, drinkCommand.getName(), drinkCommand.getAliases()).setOverrideExistingCommands(false);
        });
        commandService.registerCommands();
    }

}