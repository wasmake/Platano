package me.makecode.platano.core.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * @author Andrew Rosales (wasmake)
 */

public class RedisListener implements Runnable {

    HashMap<String, Callback> callbacks = new HashMap<>();

    private final String prefix;
    private final Jedis jedis;

    public RedisListener(String prefix, Jedis jedis){
        this.prefix = prefix;
        this.jedis = jedis;
    }

    public void subscribe(){
        jedis.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                if (channel.equals(prefix)) {
                    String[] args = message.split(Pattern.quote("|"));
                    if(callbacks.containsKey(args[0])){
                        String[] messages = Arrays.copyOfRange(args, 1, args.length);
                        String msg = Arrays.toString(messages);
                        msg = msg.substring(1, msg.length()-1);
                        System.out.println(msg);
                        callbacks.get(args[0]).onMessage(msg.split(";"));
                    }
                }
                super.onMessage(channel, message);
            }

            @Override
            public void onSubscribe(String channel, int subscribedChannels) {
                System.out.println(prefix + " successfully subscribed to redis channel.");
            }

        }, prefix);
    }

    public void addListener(String channel, Callback callback){
        System.out.println("Registering redis listener for " + prefix + " " + channel);
        callbacks.putIfAbsent(channel, callback);
    }

    @Override
    public void run() {
        subscribe();
    }

}