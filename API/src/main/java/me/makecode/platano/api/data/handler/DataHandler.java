package me.makecode.platano.api.data.handler;

import gnu.trove.set.hash.THashSet;
import lombok.Getter;
import me.makecode.platano.api.data.abstraction.DataManager;
import me.makecode.platano.api.data.object.DataAbstract;
import org.bson.types.ObjectId;

@Getter
public abstract class DataHandler implements AbstractDataHandler {

    private THashSet<DataManager> dataManagerList;

    public DataHandler(){
        dataManagerList = new THashSet<>();
    }

    public THashSet<DataManager> unloadLeave(){
        THashSet<DataManager> managersList = new THashSet<>();
        for(DataManager dataManager : dataManagerList){
            if(dataManager.isLeave()) managersList.add(dataManager);
        }
        return managersList;
    }

    public THashSet<DataManager> unloadDisable(){
        THashSet<DataManager> managersList = new THashSet<>();
        for(DataManager dataManager : dataManagerList){
            if(dataManager.isDisable()) managersList.add(dataManager);
        }
        return managersList;
    }

    public THashSet<DataManager> loadJoin(){
        THashSet<DataManager> managersList = new THashSet<>();
        for(DataManager dataManager : dataManagerList){
            if(dataManager.isJoin()) managersList.add(dataManager);
        }
        return managersList;
    }

    public THashSet<DataManager> loadStart(){
        THashSet<DataManager> managersList = new THashSet<>();
        for(DataManager dataManager : dataManagerList){
            if(dataManager.isStart()) managersList.add(dataManager);
        }
        return managersList;
    }

    public DataAbstract create(Class<?> data, Object identifier){
        for(DataManager dataManager : dataManagerList){
            if(dataManager.getData().equals(data)){
                return dataManager.create(identifier);
            }
        }
        return null;
    }

    public THashSet<DataAbstract> getCache(Class<?> data){
        for(DataManager dataManager : dataManagerList){
            if(dataManager.getData().equals(data)){
                return dataManager.getCache();
            }
        }
        return null;
    }

    public DataAbstract getViaLoad(Class<?> data, Object identifier){
        for(DataManager dataManager : dataManagerList){
            if(dataManager.getData().equals(data)){
                return dataManager.getViaLoad(identifier);
            }
        }
        return null;
    }

    public DataManager getDataManager(Class<?> data){
        for(DataManager dataManager : dataManagerList){
            if(dataManager.getData().equals(data)){
                return dataManager;
            }
        }
        return null;
    }

    public DataAbstract get(Class<?> data, Object identifier){
        for(DataManager dataManager : dataManagerList){
            if(dataManager.getData().equals(data)){
                return dataManager.get(identifier);
            }
        }
        return null;
    }

    public ObjectId getObjectId(Object objectId){
        if(ObjectId.class.isAssignableFrom(objectId.getClass())){
            return (ObjectId) objectId;
        }
        return null;
    }

}
