package me.makecode.platano.api.data.abstraction;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import lombok.Getter;
import lombok.Setter;
import me.makecode.platano.api.data.handler.DataHandler;
import me.makecode.platano.api.data.handler.DataInvalidIdentifierException;
import me.makecode.platano.api.data.object.DataAbstract;
import me.makecode.platano.api.data.object.DataLoad;
import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Getter
@Setter
public class DataManager {
    private THashMap<Object, DataAbstract> dataCache;
    private String parameter;
    private Class<?> data, from;
    private DataHandler dataHandler;
    private DataLoad dataLoad;
    private boolean cache = false, leave = false, join = false, start = false, disable = true;

    public DataManager(Class<?> data, String parameter, DataHandler dataHandler, boolean cache){
        dataCache = new THashMap<>();
        this.data = data;
        this.parameter = parameter;
        this.cache = cache;
        this.dataHandler = dataHandler;
        dataHandler.registerClass(data);
        dataHandler.getDataManagerList().add(this);
    }

    public DataManager setFrom(Class<?> clazz){
        this.from = clazz;
        return this;
    }

    public DataManager loadOperation(DataLoad dataLoad){
        this.dataLoad = dataLoad;
        return this;
    }

    public DataAbstract load(Object object){
        try {
            if(from.isAssignableFrom(object.getClass())){
                return get(dataLoad.execute(object));
            } else throw new DataInvalidIdentifierException(data.getSimpleName() + " with value " + object);
        } catch (DataInvalidIdentifierException exception){
            Bukkit.getLogger().severe(exception.getMsg());
        }
        return null;
    }

    public void removeDataAbstract(DataAbstract dataAbstract){
        dataHandler.delete(dataAbstract);
    }

    public void save(Object object){
        saveDataAbstract(load(object));
    }

    public void saveAndClose(Object object){
        Object identifierFromObject = dataLoad.execute(object);
        saveDataAbstract(load(object));
        if(cache) dataCache.remove(identifierFromObject);
    }

    public void saveWithIdentifier(Object identifier){
        dataHandler.save(get(identifier));
    }

    public void saveDataAbstract(DataAbstract dataAbstract){
        dataHandler.save(dataAbstract);
    }

    public void saveAll(){
        for(DataAbstract dataAbstract : dataCache.values()) dataHandler.save(dataAbstract);
    }

    public DataManager setCacheParams(boolean leave, boolean join, boolean start, boolean disable){
        this.leave = leave;
        this.join = join;
        this.start = start;
        this.disable = disable;
        return this;
    }

    public DataAbstract getViaLoad(Object identifier){
        return load(identifier);
    }

    public DataAbstract get(Object identifier){
        if(cache) return getCache(identifier);
        return getFromDB(identifier);
    }

    public THashSet<DataAbstract> getCache(){
        if(cache) return new THashSet<>(dataCache.values());
        return new THashSet<>();
    }

    public DataAbstract create(Object identifier){
        return getNewInstance(identifier);
    }

    public DataAbstract getFromDB(Object identifier){
        DataAbstract dataAbstract = dataHandler.getSpecific(getData(), getParameter(), identifier);
        if(dataAbstract == null){
            dataAbstract = getNewInstance(identifier);
        }
        return dataAbstract;
    }

    public List<DataAbstract> getAllFromDB(){
        List<DataAbstract> dataAbstracts = (List<DataAbstract>) dataHandler.getAll(getData());
        if(cache) {
            for(DataAbstract dataAbstract : dataAbstracts){
                this.dataCache.put(dataAbstract.getId(), dataAbstract);
            }
        }
        return dataAbstracts;
    }

    public DataAbstract getNewInstance(Object identifier){
        DataAbstract dataAbstract = null;
        try {
            Constructor constructor = getData().getConstructor(String.class);
            if(!constructor.isAccessible()) constructor.setAccessible(true);
            dataAbstract = (DataAbstract) getData().getConstructor(String.class).newInstance(identifier);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return dataAbstract;
    }

    public DataAbstract getCache(Object identifier){
        if(!dataCache.containsKey(identifier)){
            dataCache.put(identifier, getFromDB(identifier));
        }
        return dataCache.get(identifier);
    }

}
