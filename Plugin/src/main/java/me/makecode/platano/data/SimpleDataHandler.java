package me.makecode.platano.data;

import me.makecode.platano.api.data.abstraction.DataManager;
import me.makecode.platano.api.data.handler.DataHandler;
import me.makecode.platano.core.data.MongoDB;

public class SimpleDataHandler {
    private DataHandler dataHandler;

    public SimpleDataHandler(){
        this.dataHandler = new MongoDB();
    }

    public DataManager registerDataClass(Class clazz, String parameter, boolean cached){
        return new DataManager(clazz, parameter, dataHandler, cached);
    }

}
