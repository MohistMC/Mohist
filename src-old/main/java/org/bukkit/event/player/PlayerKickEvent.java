package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a player gets kicked from the server
 */
public class PlayerKickEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private String leaveMessage;
    private String kickReason;
    private Boolean cancel;

    public PlayerKickEvent(final Player playerKicked, final String kickReason, final String leaveMessage) {
        super(playerKicked);
        this.kickReason = kickReason;
        this.leaveMessage = leaveMessage;
        this.cancel = false;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the reason why the player is getting kicked
     *
     * @return string kick reason
     */
    public String getReason() {
        return kickReason;
    }

    /**
     * Sets the reason why the player is getting kicked
     *
     * @param kickReason kick reason
     */
    public void setReason(String kickReason) {
        this.kickReason = kickReason;
    }

    /**
     * Gets the leave message send to all online players
     *
     * @return string kick reason
     */
    public String getLeaveMessage() {
        return leaveMessage;
    }

    /**
     * Sets the leave message send to all online players
     *
     * @param leaveMessage leave message
     */
    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
