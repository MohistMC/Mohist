package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player gets kicked from the server
 */
public class PlayerKickEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private net.kyori.adventure.text.Component leaveMessage; // Paper
    private net.kyori.adventure.text.Component kickReason; // Paper
    private Boolean cancel;

    @Deprecated // Paper
    public PlayerKickEvent(@NotNull final Player playerKicked, @NotNull final String kickReason, @NotNull final String leaveMessage) {
        super(playerKicked);
        this.kickReason = org.bukkit.Bukkit.getUnsafe().legacyComponentSerializer().deserialize(kickReason); // Paper
        this.leaveMessage = org.bukkit.Bukkit.getUnsafe().legacyComponentSerializer().deserialize(leaveMessage); // Paper
        this.cancel = false;
    }

    // Paper start
    public PlayerKickEvent(@NotNull final Player playerKicked, @NotNull final net.kyori.adventure.text.Component kickReason, @NotNull final net.kyori.adventure.text.Component leaveMessage) {

        super(playerKicked);
        this.kickReason = kickReason;
        this.leaveMessage = leaveMessage;
        this.cancel = false;
    }

    /**
     * Gets the leave message send to all online players
     *
     * @return string kick reason
     */
    public @NotNull net.kyori.adventure.text.Component leaveMessage() {
        return this.leaveMessage;
    }

    /**
     * Sets the leave message send to all online players
     *
     * @param leaveMessage leave message
     */
    public void leaveMessage(@NotNull net.kyori.adventure.text.Component leaveMessage) {
        this.leaveMessage = leaveMessage;
    }

    /**
     * Gets the reason why the player is getting kicked
     *
     * @return string kick reason
     */
    public @NotNull net.kyori.adventure.text.Component reason() {
        return this.kickReason;
    }

    /**
     * Sets the reason why the player is getting kicked
     *
     * @param kickReason kick reason
     */
    public void reason(@NotNull net.kyori.adventure.text.Component kickReason) {
        this.kickReason = kickReason;
    }
    // Paper end

    /**
     * Gets the reason why the player is getting kicked
     *
     * @return string kick reason
     * @deprecated in favour of {@link #reason()}
     */
    @NotNull
    @Deprecated // Paper
    public String getReason() {
        return org.bukkit.Bukkit.getUnsafe().legacyComponentSerializer().serialize(this.kickReason); // Paper
    }

    /**
     * Gets the leave message send to all online players
     *
     * @return string kick reason
     * @deprecated in favour of {@link #leaveMessage()}
     */
    @NotNull
    @Deprecated // Paper
    public String getLeaveMessage() {
        return org.bukkit.Bukkit.getUnsafe().legacyComponentSerializer().serialize(this.leaveMessage); // Paper
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Sets the reason why the player is getting kicked
     *
     * @param kickReason kick reason
     * @deprecated in favour of {@link #reason(net.kyori.adventure.text.Component)}
     */
    @Deprecated // Paper
    public void setReason(@NotNull String kickReason) {
        this.kickReason = org.bukkit.Bukkit.getUnsafe().legacyComponentSerializer().deserialize(kickReason); // Paper
    }

    /**
     * Sets the leave message send to all online players
     *
     * @param leaveMessage leave message
     * @deprecated in favour of {@link #leaveMessage(net.kyori.adventure.text.Component)}
     */
    @Deprecated // Paper
    public void setLeaveMessage(@NotNull String leaveMessage) {
        this.leaveMessage = org.bukkit.Bukkit.getUnsafe().legacyComponentSerializer().deserialize(leaveMessage); // Paper
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
