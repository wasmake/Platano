package me.makecode.platano.bungee;

import lombok.Getter;
import me.makecode.platano.core.KiwiiCore;
import me.makecode.platano.bungee.command.DrinkManager;
import me.makecode.platano.bungee.listener.RedisMessagerListener;
import me.makecode.platano.core.config.WConfiguration;
import me.makecode.platano.core.dependency.MavenLibrary;
import me.makecode.platano.core.dependency.Repository;
import me.makecode.platano.core.redis.Redis;
import me.makecode.platano.core.redis.RedisConfig;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;

@Getter
@MavenLibrary(artifactId = "jedis", repo = @Repository(url = "https://oss.sonatype.org/content/groups/public/"))
@MavenLibrary(artifactId = "snakeyaml", repo = @Repository(url = "https://oss.sonatype.org/content/groups/public/"))
@MavenLibrary(artifactId = "guava", repo = @Repository(url = "https://oss.sonatype.org/content/groups/public/"))
@MavenLibrary(artifactId = "commons-pool2", repo = @Repository(url = "https://oss.sonatype.org/content/groups/public/"))
@MavenLibrary(artifactId = "slf4j-api", repo = @Repository(url = "https://oss.sonatype.org/content/groups/public/"))
public final class KiwiiBungee extends Plugin {

    private KiwiiCore kiwiiCore;
    private WConfiguration config;
    private DrinkManager drinkManager;

    public static KiwiiBungee instance;

    public void onLoad(){
        this.instance = this;
        this.kiwiiCore = new KiwiiCore("test", this, getLogger(), getDataFolder());
        this.config = new WConfiguration(new File(getDataFolder(),"config.yml"));

        config.addDefault("redis.ip", "localhost");
        config.addDefault("redis.port", 3307);
        config.addDefault("redis.password", "securepassword");
        config.saveDefaults();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.kiwiiCore.registerRedis(new Redis(new RedisConfig("Kiwii", config.getString("redis.ip"), config.getString("redis.password"), config.getInt("redis.port"), 1200, 100, 100)));
        this.kiwiiCore.registerListener(new RedisMessagerListener());

        this.kiwiiCore.publishRedis("test", "Hola");

        this.drinkManager = new DrinkManager(this);
        this.drinkManager.getClassCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
