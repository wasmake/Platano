package me.makecode.platano.core.command.api.provider;

import com.google.common.collect.ImmutableList;
import me.makecode.platano.core.command.api.argument.CommandArg;
import me.makecode.platano.core.command.api.exception.CommandExitMessage;
import me.makecode.platano.core.command.api.lang.Lang;
import me.makecode.platano.core.command.api.parametric.CommandProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

public class BooleanProvider extends CommandProvider<Boolean> {
    private final Lang lang;
    private static final List<String> SUGGEST = ImmutableList.of("true", "false");
    private static final List<String> SUGGEST_TRUE = ImmutableList.of("true");
    private static final List<String> SUGGEST_FALSE = ImmutableList.of("false");

    public BooleanProvider(@Nonnull Lang lang) {
        this.lang = lang;
    }

    @Override
    public boolean doesConsumeArgument() {
        return true;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public boolean allowNullArgument() {
        return true;
    }

    @Nullable
    @Override
    public Boolean defaultNullValue() {
        return false;
    }

    @Override
    public Boolean provide(@Nonnull CommandArg arg, @Nonnull List<? extends Annotation> annotations) throws CommandExitMessage {
        String s = arg.get();
        if (s == null) {
            return false;
        }
        try {
            return Boolean.parseBoolean(s);
        } catch (NumberFormatException ex) {
            throw new CommandExitMessage(lang.get(Lang.Type.INVALID_BOOLEAN, s));
        }
    }

    @Override
    public String argumentDescription() {
        return "true/false";
    }

    @Override
    public List<String> getSuggestions(@Nonnull String prefix) {
        prefix = prefix.toLowerCase();
        if (prefix.length() == 0) {
            return SUGGEST;
        }
        if ("true".startsWith(prefix)) {
            return SUGGEST_TRUE;
        } else if ("false".startsWith(prefix)) {
            return SUGGEST_FALSE;
        } else {
            return Collections.emptyList();
        }
    }
}
