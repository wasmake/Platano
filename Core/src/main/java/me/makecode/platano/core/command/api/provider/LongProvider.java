package me.makecode.platano.core.command.api.provider;

import me.makecode.platano.core.command.api.argument.CommandArg;
import me.makecode.platano.core.command.api.exception.CommandExitMessage;
import me.makecode.platano.core.command.api.lang.Lang;
import me.makecode.platano.core.command.api.parametric.CommandProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

public final class LongProvider extends CommandProvider<Long> {
    private final Lang lang;

    public LongProvider(@Nonnull Lang lang) {
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
        return false;
    }

    @Nullable
    @Override
    public Long defaultNullValue() {
        return 0L;
    }

    @Override
    public Long provide(@Nonnull CommandArg arg, @Nonnull List<? extends Annotation> annotations) throws CommandExitMessage {
        String s = arg.get();
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException ex) {
            throw new CommandExitMessage(lang.get(Lang.Type.INVALID_LONG, s));
        }
    }

    @Override
    public String argumentDescription() {
        return "long number";
    }

    @Override
    public List<String> getSuggestions(@Nonnull String prefix) {
        return Collections.emptyList();
    }
}
