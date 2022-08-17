package me.makecode.platano.core.event.implement;

import lombok.Getter;
import me.makecode.platano.core.event.CoreEvent;

import java.util.Optional;

@Getter
public class RedisMessageInEvent extends CoreEvent {

    private Optional<String> sender, receiver;
    private String message;

    public RedisMessageInEvent(String message){
        super();
        this.message = message;
    }

    public RedisMessageInEvent(String sender, String receiver, String message){
        super();
        this.sender = Optional.ofNullable(sender);
        this.receiver = Optional.ofNullable(receiver);
        this.message = message;
    }

    public RedisMessageInEvent setSender(String sender){
        this.sender = Optional.ofNullable(sender);
        return this;
    }

    public RedisMessageInEvent setReceiver(String receiver){
        this.receiver = Optional.ofNullable(receiver);
        return this;
    }

}
