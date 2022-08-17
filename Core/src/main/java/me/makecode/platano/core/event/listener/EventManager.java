package me.makecode.platano.core.event.listener;

import me.makecode.platano.core.event.Cancellable;
import me.makecode.platano.core.event.CoreEvent;
import me.makecode.platano.core.event.CoreListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class EventManager {
    private Map<Class<? extends CoreEvent>, List<CoreListener>> listeners;

    public EventManager(){
        listeners = new ConcurrentHashMap<>();
    }

    public void register(CoreListener listener) {
        for(Method method : listener.getClass().getDeclaredMethods()){
            if(method.getAnnotation(EventHandler.class) == null) continue;
            if(method.getParameterCount() != 1) continue;
            if(!method.getParameterTypes()[0].getSuperclass().equals(CoreEvent.class)) continue;
            Class<? extends CoreEvent> event = (Class<? extends CoreEvent>) method.getParameterTypes()[0];

            List<CoreListener> eventListeners = Optional.ofNullable(listeners.get(event)).orElse(new ArrayList<>());
            eventListeners.add(listener);
            listeners.put(event, eventListeners);
        }
    }

    public void unregister(CoreListener eventListener) {
        for(Class<? extends CoreEvent> key : listeners.keySet()){
            List<CoreListener> eventListeners = listeners.get(key);
            for(CoreListener listener : eventListeners){
                if(listener.equals(eventListener)){
                    eventListeners.remove(listener);
                    listeners.put(key, eventListeners);
                    return;
                }
            }
        }
    }

    public void callEvent(CoreEvent event) {
        if(!listeners.containsKey(event.getClass())) return;
        if(event instanceof Cancellable){
            if (((Cancellable) event).isCancelled()){
                return;
            }
        }
        for(CoreListener eventListeners : listeners.get(event.getClass())){
            this.executeEvent(event, eventListeners);
        }
    }

    private void executeEvent(CoreEvent event, CoreListener listener){
        for(Method method : listener.getClass().getDeclaredMethods()){
            if(method.getAnnotation(EventHandler.class) == null) continue;
            if(method.getParameterCount() != 1) continue;
            if(!method.getParameterTypes()[0].equals(event.getClass())) continue;
            try {
                method.invoke(listener, event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

}
