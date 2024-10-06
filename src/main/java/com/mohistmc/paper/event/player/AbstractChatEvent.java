package com.mohistmc.paper.event.player;

import com.mohistmc.paper.chat.ChatRenderer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * An abstract implementation of a chat event, handling shared logic.
 */
@ApiStatus.NonExtendable
public abstract class AbstractChatEvent extends PlayerEvent implements Cancellable {
    private final Set<Audience> viewers;
    private final Component originalMessage;
    private final SignedMessage signedMessage;
    private ChatRenderer renderer;
    private Component message;
    private boolean cancelled = false;

    AbstractChatEvent(final boolean async, final @NotNull Player player, final @NotNull Set<Audience> viewers, final @NotNull ChatRenderer renderer, final @NotNull Component message, final @NotNull Component originalMessage, final @NotNull SignedMessage signedMessage) {
        super(player, async);
        this.viewers = viewers;
        this.renderer = renderer;
        this.message = message;
        this.originalMessage = originalMessage;
        this.signedMessage = signedMessage;
    }

    /**
     * Gets a set of {@link Audience audiences} that this chat message will be displayed to.
     *
     * <p>The set returned may auto-populate on access. Any listener accessing the returned set should be aware that
     * it may reduce performance for a lazy set implementation.</p>
     *
     * @return a mutable set of {@link Audience audiences} who will receive the chat message
     */
    @NotNull
    public final Set<Audience> viewers() {
        return this.viewers;
    }

    /**
     * Sets the chat renderer.
     *
     * @param renderer the chat renderer
     * @throws NullPointerException if {@code renderer} is {@code null}
     */
    public final void renderer(final @NotNull ChatRenderer renderer) {
        this.renderer = requireNonNull(renderer, "renderer");
    }

    /**
     * Gets the chat renderer.
     *
     * @return the chat renderer
     */
    @NotNull
    public final ChatRenderer renderer() {
        return this.renderer;
    }

    /**
     * Gets the user-supplied message.
     * The return value will reflect changes made using {@link #message(Component)}.
     *
     * @return the user-supplied message
     */
    @NotNull
    public final Component message() {
        return this.message;
    }

    /**
     * Sets the user-supplied message.
     *
     * @param message the user-supplied message
     * @throws NullPointerException if {@code message} is {@code null}
     */
    public final void message(final @NotNull Component message) {
        this.message = requireNonNull(message, "message");
    }

    /**
     * Gets the original and unmodified user-supplied message.
     * The return value will <b>not</b> reflect changes made using
     * {@link #message(Component)}.
     *
     * @return the original user-supplied message
     */
    @NotNull
    public final Component originalMessage() {
        return this.originalMessage;
    }

    /**
     * Gets the signed message.
     * Changes made in this event will <b>not</b> update
     * the signed message.
     *
     * @return the signed message
     */
    @NotNull
    public final SignedMessage signedMessage() {
        return this.signedMessage;
    }

    @Override
    public final boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public final void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
}
