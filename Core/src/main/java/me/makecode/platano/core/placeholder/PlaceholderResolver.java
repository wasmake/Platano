package me.makecode.platano.core.placeholder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class PlaceholderResolver {
    private List<PlaceholderRegistrar> loadedPlaceholders;

    public PlaceholderResolver(){
        loadedPlaceholders = new ArrayList<>();
    }

    public void loadPlaceHolder(PlaceholderRegistrar placeholderRegistrar){
        loadedPlaceholders.add(placeholderRegistrar);
    }

    public String substitute(String message, String player){
        AtomicReference<String> msg = new AtomicReference<>("");
        loadedPlaceholders.forEach(placeholderRegistrar -> {
            msg.set(placeholderRegistrar.substitute(message, player));
        });
        return msg.get();
    }

}
