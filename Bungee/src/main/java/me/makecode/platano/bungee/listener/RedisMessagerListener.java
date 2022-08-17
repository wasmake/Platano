package me.makecode.platano.bungee.listener;

import me.makecode.platano.core.event.CoreListener;
import me.makecode.platano.core.event.implement.RedisMessageInEvent;
import me.makecode.platano.core.event.listener.EventHandler;

public class RedisMessagerListener implements CoreListener {

    @EventHandler
    public void getRedisMessagerEvent(RedisMessageInEvent event){
        System.out.println(event.getMessage());
    }

}
