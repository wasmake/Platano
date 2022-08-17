package me.makecode.platano.core.data;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.DateStorage;
import dev.morphia.mapping.MapperOptions;
import dev.morphia.query.Query;
import me.makecode.platano.api.data.abstraction.DataClient;
import me.makecode.platano.api.data.abstraction.DataOM;
import me.makecode.platano.api.data.abstraction.GenericDatastorage;
import me.makecode.platano.api.data.handler.DataHandler;
import me.makecode.platano.api.data.object.DataAbstract;
import org.bson.types.ObjectId;

import java.util.List;

public class MongoDB extends DataHandler {

    private DataClient dataClient;
    private DataOM dataOM;
    private GenericDatastorage genericDatastorage;

    public void connect(String... args) {
        MongoClientOptions mongoOptions = MongoClientOptions.builder()
                .connectionsPerHost(10)
                .maxConnectionIdleTime(600000)
                .socketTimeout(60000)
                .connectTimeout(15000).build();
        dataClient = new DataClient(new MongoClient(new ServerAddress(args[0], Integer.parseInt(args[1])), MongoCredential.createCredential(args[2], args[3], args[4].toCharArray()), mongoOptions));
        dataOM = new DataOM(new Morphia());

        MapperOptions options = getMorphia().getMapper().getOptions();
        options.setDateStorage(DateStorage.UTC);

        MongoClient mongoClient = (MongoClient) dataClient.getDataClient();
        genericDatastorage = new GenericDatastorage(getMorphia().createDatastore(mongoClient, args[3]));
        getMorphiaDataStore().ensureIndexes();
    }

    private Morphia getMorphia(){
        return (Morphia) getDataOM().getObjetManager();
    }

    private Datastore getMorphiaDataStore(){
        return (Datastore) getDataStorage().getDataSource();
    }

    public void save(Object entity){
        getMorphiaDataStore().save(entity);
    }

    @Override
    public void delete(Object clazz) {

    }

    @Override
    public List<DataAbstract> getAll(Class<?> clazz) {
        return createQuery(clazz).find().toList();
    }

    @Override
    public DataAbstract getSpecific(Class<?> clazz, String parameter, Object identifier) {
        ObjectId objectId = getObjectId(identifier);
        DataAbstract dataAbstract = (DataAbstract) createQuery(clazz).filter(parameter + " ==", objectId != null ? objectId : identifier).first();
        return dataAbstract;
    }

    public void registerClass(Class... classes) {
        getMorphia().map(classes);
    }

    public Query createQuery(Class clazz){
        return getMorphiaDataStore().createQuery(clazz);
    }

    @Override
    public boolean isConnected() {
        MongoClient client = (MongoClient) getClient().getDataClient();
        try {
            client.getAddress();
            return true;
        } catch (Exception e) {
            client.close();
            return false;
        }
    }

    public DataClient getClient() {
        return dataClient;
    }

    public DataOM getDataOM() {
        return dataOM;
    }

    public GenericDatastorage getDataStorage() {
        return genericDatastorage;
    }
}
