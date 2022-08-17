package me.makecode.platano.api.data.abstraction;

import lombok.Getter;

public class DataClient {
    @Getter
    private Object dataClient;
    public DataClient(Object dataClient){
        this.dataClient = dataClient;
    }
}