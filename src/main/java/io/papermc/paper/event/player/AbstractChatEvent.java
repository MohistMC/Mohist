package io.papermc.paper.event.player;

import io.papermc.paper.chat.ChatComposer;
import io.papermc.paper.chat.ChatFormatter;

import java.util.HashSet;
import java.util.Set;

import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/**
 * An abstract implementation of a chat event, handling shared logic.
 */
public abstract class AbstractChatEvent extends PlayerEvent implements Cancellable {
    private final Set<Audience> viewers;
    @Deprecated
    private final Set<Player> recipients;
    private boolean cancelled = false;
    private ChatRenderer renderer;
    @Deprecated
    private @Nullable ChatComposer composer;
    @Deprecated
    private @Nullable ChatFormatter formatter;
    private final Component originalMessage;
    private Component message;

    AbstractChatEvent(final boolean async, final @NotNull Player player, final @NotNull Set<Audience> viewers, final @NotNull ChatRenderer renderer, final @NotNull Component message, final @NotNull Component originalMessage) {
        super(player, async);
        this.viewers = viewers;
        this.recipients = new HashSet<>(Bukkit.getOnlinePlayers());
        this.renderer = renderer;
        this.message = message;
        this.originalMessage = originalMessage;
    }

    /**
     * @deprecated for removal with 1.17
     */
    @Deprecated
    AbstractChatEvent(final boolean async, final @NotNull Player player, final @NotNull Set<Audience> viewers, final @NotNull ChatRenderer renderer, final @NotNull Component message) {
        super(player, async);
        this.viewers = viewers;
        this.recipients = new HashSet<>(Bukkit.getOnlinePlayers());
        this.renderer = renderer;
        this.message = message;
        this.originalMessage = message;
    }

    /**
     * @deprecated for removal with 1.17
     */
    @Deprecated
    AbstractChatEvent(final boolean async, final @NotNull Player player, final @NotNull Set<Player> recipients, final @NotNull Set<Audience> viewers, final @NotNull ChatRenderer renderer, final @NotNull Component message, final @NotNull Component originalMessage) {
        super(player, async);
        this.recipients = recipients;
        this.viewers = viewers;
        this.renderer = renderer;
        this.message = message;
        this.originalMessage = originalMessage;
    }

    /**
     * @deprecated for removal with 1.17
     */
    @Deprecated
    AbstractChatEvent(final boolean async, final @NotNull Player player, final @NotNull Set<Player> recipients, final @NotNull Set<Audience> viewers, final @NotNull ChatRenderer renderer, final @NotNull Component message) {
        super(player, async);
        this.recipients = recipients;
        this.viewers = viewers;
        this.renderer = renderer;
        this.message = message;
        this.originalMessage = message;
    }

    /**
     * @deprecated for removal with 1.17
     */
    @Deprecated
    AbstractChatEvent(final boolean async, final @NotNull Player player, final @NotNull Set<Player> recipients, final @NotNull ChatComposer composer, final @NotNull Component message) {
        super(player, async);
        this.recipients = recipients;
        final Set<Audience> audiences = new HashSet<>(recipients);
        audiences.add(Bukkit.getConsoleSender());
        this.viewers = audiences;
        this.composer = composer;
        this.message = message;
        this.originalMessage = message;
    }

    /**
     * @deprecated for removal with 1.17
     */
    @Deprecated
    AbstractChatEvent(final boolean async, final @NotNull Player player, final @NotNull Set<Player> recipients, final @NotNull ChatFormatter formatter, final @NotNull Component message) {
        super(player, async);
        this.recipients = recipients;
        final Set<Audience> audiences = new HashSet<>(recipients);
        audiences.add(Bukkit.getConsoleSender());
        this.viewers = audiences;
        this.formatter = formatter;
        this.message = message;
        this.originalMessage = message;
    }

    /**
     * Gets a set of {@link Audience audiences} that this chat message will be displayed to.
     *
     * <p>The set returned is not guaranteed to be mutable and may auto-populate
     * on access. Any listener accessing the returned set should be aware that
     * it may reduce performance for a lazy set implementation.</p>
     *
     * <p>Listeners should be aware that modifying the list may throw {@link
     * UnsupportedOperationException} if the event caller provides an
     * unmodifiable set.</p>
     *
     * @return a set of {@link Audience audiences} who will receive the chat message
     */
    @NotNull
    public final Set<Audience> viewers() {
        return this.viewers;
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
     * @deprecated for removal with 1.17, in favor of {@link #viewers()}
     */
    @Deprecated
    @NotNull
    public final Set<Player> recipients() {
        return this.recipients;
    }

    /**
     * Sets the chat renderer.
     *
     * @param renderer the chat renderer
     * @throws NullPointerException if {@code renderer} is {@code null}
     */
    public final void renderer(final @NotNull ChatRenderer renderer) {
        this.renderer = requireNonNull(renderer, "renderer");
        this.formatter = null;
        this.composer = null;
    }

    /**
     * Gets the chat renderer.
     *
     * @return the chat renderer
     */
    @NotNull
    public final ChatRenderer renderer() {
        if (this.renderer == null) {
            if (this.composer != null) {
                this.renderer = ChatRenderer.viewerUnaware((source, displayName, message) -> this.composer.composeChat(source, source.displayName(), message));
            } else {
                requireNonNull(this.formatter, "renderer, composer, and formatter");
                this.renderer = ChatRenderer.viewerUnaware((source, displayName, message) -> this.formatter.chat(source.displayName(), message));
            }
        }
        return this.renderer;
    }

    /**
     * Gets the chat composer.
     *
     * @return the chat composer
     * @deprecated for removal with 1.17, in favour of {@link #renderer()}
     */
    @Deprecated
    @NotNull
    public final ChatComposer composer() {
        if (this.composer == null) {
            if (this.renderer != null) {
                this.composer = (source, displayName, message) -> this.renderer.render(source, displayName, message, this.legacyForwardingAudience());
            } else {
                requireNonNull(this.formatter, "renderer, composer, and formatter");
                this.composer = (source, displayName, message) -> this.formatter.chat(displayName, message);
            }
        }
        return this.composer;
    }

    /**
     * Sets the chat composer.
     *
     * @param composer the chat composer
     * @throws NullPointerException if {@code composer} is {@code null}
     * @deprecated for removal with 1.17, in favour of {@link #renderer(ChatRenderer)}
     */
    @Deprecated
    public final void composer(final @NotNull ChatComposer composer) {
        this.composer = requireNonNull(composer, "composer");
        this.formatter = null;
        this.renderer = null;
    }

    /**
     * Gets the chat formatter.
     *
     * @return the chat formatter
     * @deprecated for removal with 1.17, in favour of {@link #renderer()}
     */
    @Deprecated
    @NotNull
    public final ChatFormatter formatter() {
        if (this.formatter == null) {
            if (this.renderer != null) {
                this.formatter = (displayName, message) -> this.renderer.render(this.player, displayName, message, this.legacyForwardingAudience());
            } else {
                requireNonNull(this.composer, "renderer, composer, and formatter");
                this.formatter = (displayName, message) -> this.composer.composeChat(this.player, displayName, message);
            }
        }
        return this.formatter;
    }

    /**
     * Sets the chat formatter.
     *
     * @param formatter the chat formatter
     * @throws NullPointerException if {@code formatter} is {@code null}
     * @deprecated for removal with 1.17, in favour of {@link #renderer(ChatRenderer)}
     */
    @Deprecated
    public final void formatter(final @NotNull ChatFormatter formatter) {
        this.formatter = requireNonNull(formatter, "formatter");
        this.composer = null;
        this.renderer = null;
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

    @Override
    public final boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public final void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    private @NotNull Audience legacyForwardingAudience() {
        return new ForwardingAudience() {
            @Override
            public @NonNull Iterable<? extends Audience> audiences() {
                return AbstractChatEvent.this.viewers;
            }
        };
    }
}
