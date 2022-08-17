package me.makecode.platano.core.command.api.parametric;

import me.makecode.platano.core.command.api.command.AbstractCommandService;
import me.makecode.platano.core.command.api.command.WrappedCommand;
import me.makecode.platano.core.command.api.exception.CommandStructureException;
import me.makecode.platano.core.command.api.exception.MissingProviderException;

public final class ProviderAssigner {
    private final AbstractCommandService<?> commandService;

    public ProviderAssigner(AbstractCommandService<?> commandService) {
        this.commandService = commandService;
    }

    public CommandProvider<?>[] assignProvidersFor(WrappedCommand wrappedCommand) throws MissingProviderException, CommandStructureException {
        CommandParameters parameters = wrappedCommand.getParameters();
        CommandProvider<?>[] providers = new CommandProvider<?>[parameters.getParameters().length];
        for (int i = 0; i < parameters.getParameters().length; i++) {
            CommandParameter param = parameters.getParameters()[i];
            if (param.isRequireLastArg() && !parameters.isLastArgument(i)) {
                throw new CommandStructureException("Parameter " + param.getParameter().getName() + " [argument " + i + "] (" + param.getParameter().getType().getSimpleName() + ") in method '" + wrappedCommand.getMethod().getName() + "' must be the last argument in the method.");
            }
            BindingContainer<?> bindings = commandService.getBindingsFor(param.getType());
            if (bindings != null) {
                CommandProvider<?> provider = null;
                for (CommandBinding<?> binding : bindings.getBindings()) {
                    if (binding.canProvideFor(param)) {
                        provider = binding.getProvider();
                        break;
                    }
                }
                if (provider != null) {
                    providers[i] = provider;
                } else {
                    throw new MissingProviderException("No provider bound for " + param.getType().getSimpleName() + " for parameter " + i + " for method " + wrappedCommand.getMethod().getName());
                }
            } else {
                throw new MissingProviderException("No provider bound for " + param.getType().getSimpleName());
            }
        }
        return providers;
    }
}
