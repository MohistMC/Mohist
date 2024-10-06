package com.mohistmc.paper.event.player;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This event is fired when the server decorates a component for chat purposes. This is called
 * before {@link AsyncChatEvent} and the other chat events. It is recommended that you modify the
 * message here, and use the chat events for modifying receivers and later the chat type. If you
 * want to keep the message as "signed" for the clients who get it, be sure to include the entire
 * original message somewhere in the final message.
 * @see AsyncChatCommandDecorateEvent for the decoration of messages sent via commands
 */
@ApiStatus.Experimental
public class AsyncChatDecorateEvent extends ServerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;
    private final Component originalMessage;
    private Component result;
    private boolean cancelled;

    @ApiStatus.Internal
    public AsyncChatDecorateEvent(final boolean async, final @Nullable Player player, final @NotNull Component originalMessage, final @NotNull Component result) {
        super(async);
        this.player = player;
        this.originalMessage = originalMessage;
        this.result = result;
    }

    /**
     * Gets the player (if available) associated with this event.
     * <p>
     * Certain commands request decorations without a player context
     * which is why this is possibly null.
     *
     * @return the player or null
     */
    public @Nullable Player player() {
        return this.player;
    }

    /**
     * Gets the original decoration input
     *
     * @return the input
     */
    public @NotNull Component originalMessage() {
        return this.originalMessage;
    }

    /**
     * Gets the decoration result. This may already be different from
     * {@link #originalMessage()} if some other listener to this event
     * <b>OR</b> the legacy preview event ({@link org.bukkit.event.player.AsyncPlayerChatPreviewEvent}
     * changed the result.
     *
     * @return the result
     */
    public @NotNull Component result() {
        return this.result;
    }

    /**
     * Sets the resulting decorated component.
     *
     * @param result the result
     */
    public void result(@NotNull Component result) {
        this.result = result;
    }

    /**
     * If this decorating is part of a preview request/response.
     *
     * @return true if part of previewing
     * @deprecated chat preview was removed in 1.19.3
     */
    @Deprecated(forRemoval = true)
    @ApiStatus.ScheduledForRemoval(inVersion = "1.21")
    @Contract(value = "-> false", pure = true)
    public boolean isPreview() {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * A cancelled decorating event means that no changes to the result component
     * will have any effect. The decorated component will be equal to the original
     * component.
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
