package io.papermc.paper.event.player;

import io.papermc.paper.chat.ChatComposer;
import io.papermc.paper.chat.ChatFormatter;

import java.util.Set;

import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * An event fired when a {@link Player} sends a chat message to the server.
 */
public final class AsyncChatEvent extends AbstractChatEvent {
    private static final HandlerList HANDLERS = new HandlerList();

    public AsyncChatEvent(final boolean async, final @NotNull Player player, final @NotNull Set<Audience> viewers, final @NotNull ChatRenderer renderer, final @NotNull Component message, final @NotNull Component originalMessage) {
        super(async, player, viewers, renderer, message, originalMessage);
    }

    /**
     * @deprecated for removal with 1.17, use {@link #AsyncChatEvent(boolean, Player, Set, ChatRenderer, Component, Component)}
     */
    @Deprecated
    public AsyncChatEvent(final boolean async, final @NotNull Player player, final @NotNull Set<Audience> viewers, final @NotNull ChatRenderer renderer, final @NotNull Component message) {
        super(async, player, viewers, renderer, message);
    }

    /**
     * @deprecated for removal with 1.17, use {@link #AsyncChatEvent(boolean, Player, Set, ChatRenderer, Component, Component)}
     */
    @Deprecated
    public AsyncChatEvent(final boolean async, final @NotNull Player player, final @NotNull Set<Player> recipients, final @NotNull Set<Audience> viewers, final @NotNull ChatRenderer renderer, final @NotNull Component message, final @NotNull Component originalMessage) {
        super(async, player, recipients, viewers, renderer, message, originalMessage);
    }

    /**
     * @deprecated for removal with 1.17, use {@link #AsyncChatEvent(boolean, Player, Set, ChatRenderer, Component, Component)}
     */
    @Deprecated
    public AsyncChatEvent(final boolean async, final @NotNull Player player, final @NotNull Set<Player> recipients, final @NotNull Set<Audience> viewers, final @NotNull ChatRenderer renderer, final @NotNull Component message) {
        super(async, player, recipients, viewers, renderer, message);
    }

    /**
     * @deprecated for removal with 1.17, use {@link #AsyncChatEvent(boolean, Player, Set, ChatRenderer, Component, Component)}
     */
    @Deprecated
    public AsyncChatEvent(final boolean async, final @NotNull Player player, final @NotNull Set<Player> recipients, final @NotNull ChatComposer composer, final @NotNull Component message) {
        super(async, player, recipients, composer, message);
    }

    /**
     * @deprecated for removal with 1.17, use {@link #AsyncChatEvent(boolean, Player, Set, ChatRenderer, Component, Component)}
     */
    @Deprecated
    public AsyncChatEvent(final boolean async, final @NotNull Player player, final @NotNull Set<Player> recipients, final @NotNull ChatFormatter formatter, final @NotNull Component message) {
        super(async, player, recipients, formatter, message);
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
