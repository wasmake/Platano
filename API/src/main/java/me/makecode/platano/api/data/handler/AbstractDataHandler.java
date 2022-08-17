package me.makecode.platano.api.data.handler;

import me.makecode.platano.api.data.abstraction.DataOM;
import me.makecode.platano.api.data.abstraction.GenericDatastorage;
import me.makecode.platano.api.data.object.DataAbstract;

import java.util.List;

public interface AbstractDataHandler {
    void connect(String... args);
    void registerClass(Class... classes);
    void save(Object clazz);
    void delete(Object clazz);
    List<DataAbstract> getAll(Class<?> clazz);
    DataAbstract getSpecific(Class<?> clazz, String paramenter, Object identifier);
    boolean isConnected();
    DataOM getDataOM();
    GenericDatastorage getDataStorage();
}
