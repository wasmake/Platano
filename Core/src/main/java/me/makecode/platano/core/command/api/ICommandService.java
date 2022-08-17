package me.makecode.platano.core.command.api;

import me.makecode.platano.core.command.api.annotation.Command;
import me.makecode.platano.core.command.api.annotation.Modifier;
import me.makecode.platano.core.command.api.authorizer.IAuthorizer;
import me.makecode.platano.core.command.api.modifier.ICommandModifier;
import me.makecode.platano.core.command.api.parametric.CommandProvider;
import me.makecode.platano.core.command.api.parametric.binder.CommandBinder;
import me.makecode.platano.core.command.api.command.CommandContainer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;

public interface ICommandService {

    /**
     * Register a command into the Command Service
     * @param handler Object that has the {@link Command} annotated methods
     * @param name The name of the command to register.
     *             The names of methods within the handler object will be sub-commands to this name.
     *             If you want to create a default command (just /name), set the name here and in the
     * {@link Command} annotation set name = ""
     * @param aliases (Optional) A list of alternate command names that can be used
     * @return The {@link CommandContainer} containing the command you registered
     */
    CommandContainer register(@Nonnull Object handler, @Nonnull String name, @Nullable String... aliases);

    /**
     * Register a sub-command into the specified root command container
     *
     * @param root    The {@link CommandContainer} super-command to register your sub-commands into
     * @param handler The object that has the {@link Command}
     *                annotated methods to register
     * @return The {@link CommandContainer} containing the command you registered (same as the root passed in)
     */
    CommandContainer registerSub(@Nonnull CommandContainer root, @Nonnull Object handler);

    /**
     * Must be called after all of you commands have been registered with
     * {@link #register(Object, String, String...)} and {@link #registerSub(CommandContainer, Object)}
     *
     * This registers the command into the Bukkit/Spigot CommandMap so that they can be executed on the server.
     */
    void registerCommands();

    /**
     * Start binding a class type to a {@link CommandProvider} or instance.
     * @param type The Class type to bind to
     * @param <T> The type of class
     * @return A {@link CommandBinder} instance to finish the binding
     */
    <T> CommandBinder<T> bind(@Nonnull Class<T> type);

    /**
     * Registers a modifier to modify provided arguments for a specific type
     * @param annotation The annotation to use for the modifier (must have {@link Modifier} annotated in it's class)
     * @param type The type to modify
     * @param modifier The modifier
     * @param <T> The type of class to modify
     */
    <T> void registerModifier(@Nonnull Class<? extends Annotation> annotation, @Nonnull Class<T> type, @Nonnull ICommandModifier<T> modifier);

    /**
     *
     * @param name The primary name of the {@link CommandContainer} you want to get
     * @return {@link Nullable} The {@link CommandContainer} with the specified name
     */
    @Nullable
    CommandContainer get(@Nonnull String name);

    /**
     * Set the authorizer that is used.
     * @param authorizer {@link Nonnull} A {@link IAuthorizer} instance to be used for
     *                                  checking authorization for command execution
     */
    void setAuthorizer(@Nonnull IAuthorizer<?> authorizer);

}
