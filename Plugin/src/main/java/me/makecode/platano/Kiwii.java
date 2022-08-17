package me.makecode.platano;

import lombok.Getter;
import me.makecode.platano.command.DrinkManager;
import me.makecode.platano.core.KiwiiCore;
import me.makecode.platano.core.config.WConfiguration;
import me.makecode.platano.core.dependency.MavenLibrary;
import me.makecode.platano.core.dependency.Repository;
import me.makecode.platano.listener.RedisMessagerListener;
import me.makecode.platano.core.placeholder.PlaceholderResolver;
import me.makecode.platano.core.redis.Redis;
import me.makecode.platano.core.redis.RedisConfig;
import me.makecode.platano.util.RedisPlaceholders;
import me.makecode.platano.util.SyncLambda;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
@MavenLibrary(artifactId = "jedis", repo = @Repository(url = "https://oss.sonatype.org/content/groups/public/"))
@MavenLibrary(artifactId = "commons-pool2", repo = @Repository(url = "https://oss.sonatype.org/content/groups/public/"))
public final class Kiwii extends JavaPlugin {

    private WConfiguration kConfig;

    private PlaceholderResolver placeholderResolver;

    private DrinkManager drinkManager;
    private KiwiiCore kiwiiCore;

    public void onLoad(){
        this.kConfig = new WConfiguration(new File(getDataFolder(),"config.yml"));

        kConfig.addDefault("servername", "myserver123", "This name should be unic and not repeated!");
        kConfig.addDefault("redis.ip", "localhost");
        kConfig.addDefault("redis.port", 3307);
        kConfig.addDefault("redis.password", "securepassword");
        kConfig.saveDefaults();

        String servername = kConfig.getString("servername");
        this.kiwiiCore = new KiwiiCore("Kiwii-"+servername, this, getLogger(), getDataFolder());

    }

    public void onEnable() {

        this.kiwiiCore.registerRedis(new Redis(new RedisConfig("Kiwii", kConfig.getString("redis.ip"), kConfig.getString("redis.password"), kConfig.getInt("redis.port"), 1200, 100, 100)));
        this.kiwiiCore.registerListener(new RedisMessagerListener());

        drinkManager = new DrinkManager(this);
        this.drinkManager.getClassCommands();

        this.placeholderResolver = new PlaceholderResolver();
        this.placeholderResolver.loadPlaceHolder(new RedisPlaceholders());
    }

    public void onDisable() {
        // Plugin shutdown logic
    }

    public void runSync(SyncLambda syncLambda){
        Bukkit.getScheduler().runTask(this, () -> syncLambda.execute());
    }

}
