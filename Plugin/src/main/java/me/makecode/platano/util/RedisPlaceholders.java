package me.makecode.platano.util;

import me.makecode.platano.core.placeholder.Placeholder;
import me.makecode.platano.core.placeholder.PlaceholderRegistrar;

public class RedisPlaceholders extends PlaceholderRegistrar {

    public RedisPlaceholders(){
        hook(this);
    }

    @Placeholder(placeholder = "player")
    public String getPlayerName(String player){
        return player;
    }

}
