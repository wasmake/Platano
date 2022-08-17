package me.makecode.platano.api.data.object;

@FunctionalInterface
public interface DataLoad {
    Object execute(Object object);
}