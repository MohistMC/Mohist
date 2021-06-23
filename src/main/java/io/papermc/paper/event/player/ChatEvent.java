package io.papermc.paper.event.player;

import io.papermc.paper.chat.ChatComposer;
import io.papermc.paper.chat.ChatFormatter;

import java.util.Set;

import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Warning;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * An event fired when a {@link Player} sends a chat message to the server.
 *
 * @deprecated Listening to this event forces chat to wait for the main thread, delaying chat messages. It is recommended to use {@link AsyncChatEvent} instead, wherever possible.
 */
@Deprecated
@Warning(reason = "Listening to this event forces chat to wait for the main thread, delaying chat messages.")
public final class ChatEvent extends AbstractChatEvent {
    private static final HandlerList HANDLERS = new HandlerList();

    public ChatEvent(final @NotNull Player player, final @NotNull Set<Audience> viewers, final @NotNull ChatRenderer renderer, final @NotNull Component message, final @NotNull Component originalMessage) {
        super(false, player, viewers, renderer, message, originalMessage);
    }

    /**
     * @deprecated for removal with 1.17, use {@link #ChatEvent(Player, Set, ChatRenderer, Component, Component)}
     */
    @Deprecated
    public ChatEvent(final @NotNull Player player, final @NotNull Set<Audience> viewers, final @NotNull ChatRenderer renderer, final @NotNull Component message) {
        super(false, player, viewers, renderer, message);
    }

    /**
     * @deprecated for removal with 1.17, use {@link #ChatEvent(Player, Set, ChatRenderer, Component, Component)}
     */
    @Deprecated
    public ChatEvent(final @NotNull Player player, final @NotNull Set<Player> recipients, final @NotNull Set<Audience> viewers, final @NotNull ChatRenderer renderer, final @NotNull Component message, final @NotNull Component originalMessage) {
        super(false, player, recipients, viewers, renderer, message, originalMessage);
    }

    /**
     * @deprecated for removal with 1.17, use {@link #ChatEvent(Player, Set, ChatRenderer, Component, Component)}
     */
    @Deprecated
    public ChatEvent(final @NotNull Player player, final @NotNull Set<Player> recipients, final @NotNull ChatComposer composer, final @NotNull Component message) {
        super(false, player, recipients, composer, message);
    }

    /**
     * @deprecated for removal with 1.17, use {@link #ChatEvent(Player, Set, ChatRenderer, Component, Component)}
     */
    @Deprecated
    public ChatEvent(final @NotNull Player player, final @NotNull Set<Player> recipients, final @NotNull ChatFormatter formatter, final @NotNull Component message) {
        super(false, player, recipients, formatter, message);
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}