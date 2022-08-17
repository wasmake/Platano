package me.makecode.platano.api.data.object;

import dev.morphia.annotations.Id;
import dev.morphia.annotations.PostLoad;
import dev.morphia.annotations.PostPersist;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Date;

@Getter
@Setter
public class DataAbstract {

    @Id
    private ObjectId id;

    private Date createdAt, updatedAt, lastLoadedAt;
    private transient  Class<?> clazz;

    public DataAbstract(Class<?> clazz){
        this.createdAt = new Date();
        this.clazz = clazz;
    }

    @PostLoad
    public void onLoad(){
        this.lastLoadedAt = new Date();
    }

    @PostPersist
    public void onSave(){
        this.updatedAt = new Date();
    }


}
