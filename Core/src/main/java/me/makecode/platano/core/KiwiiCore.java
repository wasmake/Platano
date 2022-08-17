package me.makecode.platano.core;

import lombok.Getter;
import me.makecode.platano.core.dependency.LibraryLoader;
import me.makecode.platano.core.dependency.MavenLibrary;
import me.makecode.platano.core.dependency.Repository;
import me.makecode.platano.core.event.CoreEvent;
import me.makecode.platano.core.event.CoreListener;
import me.makecode.platano.core.event.implement.RedisMessageInEvent;
import me.makecode.platano.core.event.listener.EventManager;
import me.makecode.platano.core.logger.UtilLogger;
import me.makecode.platano.core.redis.Redis;

import java.io.File;

@Getter
@MavenLibrary(artifactId = "trove", repo = @Repository(url = "http://zoidberg.ukp.informatik.tu-darmstadt.de/artifactory/public-releases/"))
public class KiwiiCore {

    private EventManager eventManager;
    private String serverName;
    private Redis redis;
    public static UtilLogger logger;

    public KiwiiCore(String serverName, Object mainClass, Object logger, File dataFolder){
        this.serverName = serverName;
        this.logger = new UtilLogger(logger);
        LibraryLoader libraryLoader = new LibraryLoader(getClass().getClassLoader(), logger, dataFolder, "me.makecode", "Kiwii-core");
        libraryLoader.loadAll(this);
        libraryLoader.loadAll(mainClass);
        this.eventManager = new EventManager();
    }

    public void registerRedis(Redis redis){
        this.redis = redis;
        redis.registerListener(serverName, listener -> {
            callEvent(new RedisMessageInEvent(listener[0], listener[1], listener[2]));
        });
        redis.registerListener("Kiwii", listener -> {
            callEvent(new RedisMessageInEvent(listener[0]));
        });
    }

    public void publishRedis(String toServer,String message){
        if(!toServer.isEmpty()) this.redis.publish("Kiwii" + toServer, serverName, toServer, message);
        else this.redis.publish("Kiwii" + toServer, message);
    }

    public void registerListener(CoreListener coreListener){
        this.eventManager.register(coreListener);
    }

    public void callEvent(CoreEvent coreEvent){
        this.eventManager.callEvent(coreEvent);
    }

}
