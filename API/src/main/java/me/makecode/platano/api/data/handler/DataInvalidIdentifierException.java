package me.makecode.platano.api.data.handler;

import lombok.Getter;

@Getter
public class DataInvalidIdentifierException extends RuntimeException {

    private String msg;
    public DataInvalidIdentifierException(String msg){
        super("Invalid Identifier when trying to get data " + msg);
        this.msg = "Invalid Identifier when trying to get data " + msg;
    }
}

