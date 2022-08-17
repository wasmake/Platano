package me.makecode.platano.core.command.api.modifier;

import lombok.Getter;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Getter
public class ModifierContainer {

    private final ConcurrentMap<Class<?>, Set<ICommandModifier<?>>> modifiers = new ConcurrentHashMap<>();

    @Nullable
    public Set<ICommandModifier<?>> getModifiersFor(Class<?> type) {
        if (modifiers.containsKey(type)) {
            return modifiers.get(type);
        }
        for (Class<?> modifierType : modifiers.keySet()) {
            if (modifierType.isAssignableFrom(type) || type.isAssignableFrom(modifierType)) {
                return modifiers.get(modifierType);
            }
        }
        return null;
    }

}
