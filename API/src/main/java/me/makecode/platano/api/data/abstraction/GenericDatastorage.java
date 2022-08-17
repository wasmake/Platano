package me.makecode.platano.api.data.abstraction;

import lombok.Getter;

public class GenericDatastorage {
    @Getter
    private Object dataSource;
    public GenericDatastorage(Object dataSource){
        this.dataSource = dataSource;
    }
}
