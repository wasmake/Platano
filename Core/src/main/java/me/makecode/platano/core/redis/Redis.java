package me.makecode.platano.core.redis;

import me.makecode.platano.core.KiwiiCore;
import me.makecode.platano.core.logger.UtilLogger;
import redis.clients.jedis.Jedis;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Andrew Rosales (wasmake)
 */

public class Redis {
    private final UtilLogger logger = KiwiiCore.logger;

    private RedisListener redisListener;
    private RedisCache redisCache;
    private final RedisPool redisPool;

    public Redis(RedisConfig redisConfig){

        Jedis jedis = new Jedis(redisConfig.getHost(), redisConfig.getPort(), redisConfig.getTimeout());
        if(redisConfig.getPassword() != null || !redisConfig.getPassword().isEmpty()) jedis.auth(redisConfig.getPassword());

        this.redisPool = new RedisPool(redisConfig);

        if(!redisPool.isConnected()){
            logger.info("Failed to connect to redis!");
            return;
        } else {
            this.redisListener = new RedisListener(redisConfig.getPrefix(), jedis);
            this.redisCache = new RedisCache(redisPool.getJedisPool(), redisConfig.getPrefix());
            subscribe();
        }
    }

    public void publish(String key, String... objects){
        StringBuilder s = new StringBuilder();

        for(String obj : objects){
            s.append(";").append(obj);
        }

        publish(key, s.substring(1));
    }

    public void publish(String key, String msg){
        System.out.println("Publishing msg " + msg + " to channel " + key);
        this.redisPool.publish(key, msg);
    }

    public void subscribe(){
        if(redisPool.isConnected()) {
            ExecutorService service = Executors.newCachedThreadPool();
            service.execute(redisListener);
        }
    }

    public void registerListener(String channel, Callback callback){
        redisListener.addListener(channel, callback);
    }

    public RedisCache getCache(){
        return this.redisCache;
    }

}