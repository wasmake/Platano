package me.makecode.platano.bungee.command;

import lombok.Getter;
import me.makecode.platano.bungee.KiwiiBungee;

@Getter
public class DrinkAbstractcommand {
    private String name;
    private String[] aliases;

    private final KiwiiBungee kiwiiBungee = KiwiiBungee.instance;

    public DrinkAbstractcommand(String name, String... aliases){
        this.name = name;
        this.aliases = aliases;
        kiwiiBungee.getDrinkManager().getCommands().add(this);
    }

}

