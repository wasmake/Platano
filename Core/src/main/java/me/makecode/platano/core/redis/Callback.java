package me.makecode.platano.core.redis;

@FunctionalInterface
public interface Callback {

    void onMessage(String[] args);

}