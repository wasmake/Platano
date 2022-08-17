package me.makecode.platano.bungee.command.admin;

import me.makecode.platano.bungee.command.DrinkAbstractcommand;
import me.makecode.platano.core.command.api.annotation.Command;
import me.makecode.platano.core.command.api.annotation.Sender;
import me.makecode.platano.core.command.api.annotation.Text;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class StoreCmd extends DrinkAbstractcommand {

    public StoreCmd() {
        super("storecmd");
    }

    @Command(name = "", desc = "Send a command to execution")
    public void getStoreCommand(@Sender CommandSender executor, String server, String player, @Text String commandArg){
        if(!(executor instanceof ProxiedPlayer)){
            String command = String.join(" ", commandArg);
            if(server.equalsIgnoreCase("all")){
                getKiwiiBungee().getKiwiiCore().publishRedis( "", player + "$" + command);
                executor.sendMessage("Command sent to execution on all servers connected.");
            } else {
                getKiwiiBungee().getKiwiiCore().publishRedis( "-" + server, player + "$" + command);
                executor.sendMessage("Command sent to " + server + " if the server exists, it should execute correctly the command.");
            }
        } else {
            executor.sendMessage("This command is only available for the console!");
        }

    }

}
