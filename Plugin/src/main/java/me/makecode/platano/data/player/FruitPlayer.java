package me.makecode.platano.data.player;

import dev.morphia.annotations.Entity;
import lombok.Getter;
import lombok.Setter;
import me.makecode.platano.api.data.object.DataAbstract;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity(value = "platano_players", noClassnameStored = true)
public class FruitPlayer extends DataAbstract {

    private String uuid, name;
    private boolean premium;

    private String firstIp;
    private String hashed_password;

    private List<String> knownIps;

    private Date lastSuccessfulLogin;

    public FruitPlayer(){
        super(FruitPlayer.class);
    }

}
