package me.makecode.platano.core.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

public class RedisCache {

    private final JedisPool jedisPool;
    private String prefix;

    private Set<String> localCache;

    public RedisCache(JedisPool jedisPool, String prefix){
        this.jedisPool = jedisPool;
        this.prefix = prefix;
        this.localCache = new HashSet<>();
    }

    public void disposeLocal(){
        localCache.forEach(this::dispose);
        localCache.clear();
    }

    public void dispose(String key){
        try (Jedis jedis = getResource()) {
            Set<String> keyList = jedis.keys(key);
            System.out.println("Key " + key);
            keyList.forEach(jedis::del);
        }
    }

    public Jedis getResource(){
        return this.jedisPool.getResource();
    }

    public String get(String key){
        try (Jedis jedis = getResource()) {
            return jedis.get(prefix + "."+key);
        }
    }

    public void set(String key, String value){
        try (Jedis jedis = getResource()) {
            localCache.add(prefix + "."+key);
            jedis.set(prefix + "."+key, value);
        }
    }

    public void remove(String key){
        try (Jedis jedis = getResource()) {
            localCache.remove(prefix + "."+key);
            jedis.del(prefix + "."+key);
        }
    }

}