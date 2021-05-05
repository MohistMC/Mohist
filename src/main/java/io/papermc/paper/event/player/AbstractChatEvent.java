package io.papermc.paper.event.player;

import io.papermc.paper.chat.ChatComposer;
import io.papermc.paper.chat.ChatFormatter;

import java.util.Set;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerEvent;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/**
 * An abstract implementation of a chat event, handling shared logic.
 */
public abstract class AbstractChatEvent extends PlayerEvent implements Cancellable {
    private final Set<Player> recipients;
    private boolean cancelled = false;
    private ChatComposer composer;
    @Deprecated
    private @Nullable ChatFormatter formatter;
    private Component message;

    AbstractChatEvent(final boolean async, final @NotNull Player player, final @NotNull Set<Player> recipients, final @NotNull ChatComposer composer, final @NotNull Component message) {
        super(player, async);
        this.recipients = recipients;
        this.composer = composer;
        this.message = message;
    }

    @Deprecated
    AbstractChatEvent(final boolean async, final @NotNull Player player, final @NotNull Set<Player> recipients, final @NotNull ChatFormatter formatter, final @NotNull Component message) {
        super(player, async);
        this.recipients = recipients;
        this.formatter = formatter;
        this.message = message;
    }

    /**
     * Gets a set of recipients that this chat message will be displayed to.
     *
     * <p>The set returned is not guaranteed to be mutable and may auto-populate
     * on access. Any listener accessing the returned set should be aware that
     * it may reduce performance for a lazy set implementation.</p>
     *
     * <p>Listeners should be aware that modifying the list may throw {@link
     * UnsupportedOperationException} if the event caller provides an
     * unmodifiable set.</p>
     *
     * @return a set of players who will receive the chat message
     */
    @NotNull
    public final Set<Player> recipients() {
        return this.recipients;
    }

    /**
     * Gets the chat composer.
     *
     * @return the chat composer
     */
    @NotNull
    public final ChatComposer composer() {
        if (this.composer == null) {
            requireNonNull(this.formatter, "composer and formatter");
            this.composer = (source, displayName, message) -> this.formatter.chat(displayName, message);
        }
        return this.composer;
    }

    /**
     * Sets the chat composer.
     *
     * @param composer the chat composer
     * @throws NullPointerException if {@code composer} is {@code null}
     */
    public final void composer(final @NotNull ChatComposer composer) {
        this.composer = requireNonNull(composer, "composer");
        this.formatter = null;
    }

    /**
     * Gets the chat formatter.
     *
     * @return the chat formatter
     * @deprecated in favour of {@link #composer()}
     */
    @Deprecated
    @NotNull
    public final ChatFormatter formatter() {
        if (this.formatter == null) {
            this.formatter = (displayName, message) -> this.composer.composeChat(this.player, displayName, message);
        }
        return this.formatter;
    }

    /**
     * Sets the chat formatter.
     *
     * @param formatter the chat formatter
     * @throws NullPointerException if {@code formatter} is {@code null}
     * @deprecated in favour of {@link #composer(ChatComposer)}
     */
    @Deprecated
    public final void formatter(final @NotNull ChatFormatter formatter) {
        this.formatter = requireNonNull(formatter, "formatter");
        this.composer = (source, displayName, message) -> formatter.chat(displayName, message);
    }

    /**
     * Gets the user-supplied message.
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

    @Override
    public final boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public final void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
}
