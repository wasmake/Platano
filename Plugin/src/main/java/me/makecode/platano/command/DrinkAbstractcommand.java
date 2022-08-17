package me.makecode.platano.command;

import lombok.Getter;
import me.makecode.platano.Kiwii;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class DrinkAbstractcommand {
    private String name;
    private String[] aliases;

    private final Kiwii kiwii = JavaPlugin.getPlugin(Kiwii.class);

    public DrinkAbstractcommand(String name, String... aliases){
        this.name = name;
        this.aliases = aliases;
        kiwii.getDrinkManager().getCommands().add(this);
    }

}

