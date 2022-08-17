package me.makecode.platano.core.command.api.provider;

import com.google.common.collect.Lists;
import me.makecode.platano.core.command.api.exception.CommandExitMessage;
import me.makecode.platano.core.command.api.parametric.CommandProvider;
import org.apache.commons.lang3.StringUtils;
import me.makecode.platano.core.command.api.argument.CommandArg;
import me.makecode.platano.core.command.api.lang.Lang;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.regex.Pattern;

public final class EnumProvider<T extends Enum<T>> extends CommandProvider<T> {
    private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^A-Za-z0-9]");

    private final Lang lang;
    private final Class<T> enumClass;

    public EnumProvider(@Nonnull Lang lang, @Nonnull Class<T> enumClass) {
        this.lang = lang;
        this.enumClass = enumClass;
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
    public T provide(@Nonnull CommandArg arg, @Nonnull List<? extends Annotation> annotations) throws CommandExitMessage {
        String name = arg.get();
        String s = simplify(name);

        for (T entry : enumClass.getEnumConstants()) {
            if (simplify(entry.name()).equalsIgnoreCase(s)) {
                return entry;
            }
        }
        throw new CommandExitMessage(lang.get(Lang.Type.INVALID_ENUM_VALUE, argumentDescription(), StringUtils.join(getSuggestions(""), ' ')));
    }

    @Override
    public String argumentDescription() {
        return enumClass.getSimpleName();
    }

    @Override
    public List<String> getSuggestions(@Nonnull String prefix) {
        List<String> suggestions = Lists.newArrayList();
        String test = simplify(prefix);

        for (T entry : enumClass.getEnumConstants()) {
            String name = simplify(entry.name());
            if (test.length() == 0 || name.startsWith(test)) {
                suggestions.add(entry.name().toLowerCase());
            }
        }

        return suggestions;
    }

    private static String simplify(String t) {
        return NON_ALPHANUMERIC.matcher(t.toLowerCase()).replaceAll("");
    }
}
