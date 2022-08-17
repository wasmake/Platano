package me.makecode.platano.api.data.abstraction;

import lombok.Getter;
import lombok.Setter;

@Getter
public class DataOM {
    private Object objetManager;
    @Setter
    private Object dataStore;
    public DataOM(Object objectManager){
        this.objetManager = objectManager;
    }

}