package com.destroystokyo.paper.event.player;

import org.apache.commons.lang.Validate;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * This event is fired during a player handshake.
 *
 * <p>If there are no listeners listening to this event, the logic default
 * to your server platform will be ran.</p>
 *
 * <p>WARNING: TAMPERING WITH THIS EVENT CAN BE DANGEROUS</p>
 */
public class PlayerHandshakeEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final String originalHandshake;
    private boolean cancelled;
    private String serverHostname;
    private String socketAddressHostname;
    private UUID uniqueId;
    private String propertiesJson;
    private boolean failed;
    private String failMessage = "If you wish to use IP forwarding, please enable it in your BungeeCord config as well!";

    /**
     * Creates a new {@link PlayerHandshakeEvent}.
     *
     * @param originalHandshake the original handshake string
     * @param cancelled         if this event is enabled
     */
    public PlayerHandshakeEvent(String originalHandshake, boolean cancelled) {
        this.originalHandshake = originalHandshake;
        this.cancelled = cancelled;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    /**
     * Determines if this event is cancelled.
     *
     * <p>When this event is cancelled, custom handshake logic will not
     * be processed.</p>
     *
     * @return {@code true} if this event is cancelled, {@code false} otherwise
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Sets if this event is cancelled.
     *
     * <p>When this event is cancelled, custom handshake logic will not
     * be processed.</p>
     *
     * @param cancelled {@code true} if this event is cancelled, {@code false} otherwise
     */
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Gets the original handshake string.
     *
     * @return the original handshake string
     */
    public String getOriginalHandshake() {
        return this.originalHandshake;
    }

    /**
     * Gets the server hostname string.
     *
     * <p>This should not include the port.</p>
     *
     * @return the server hostname string
     */
    public String getServerHostname() {
        return this.serverHostname;
    }

    /**
     * Sets the server hostname string.
     *
     * <p>This should not include the port.</p>
     *
     * @param serverHostname the server hostname string
     */
    public void setServerHostname(String serverHostname) {
        this.serverHostname = serverHostname;
    }

    /**
     * Gets the socket address hostname string.
     *
     * <p>This should not include the port.</p>
     *
     * @return the socket address hostname string
     */
    public String getSocketAddressHostname() {
        return this.socketAddressHostname;
    }

    /**
     * Sets the socket address hostname string.
     *
     * <p>This should not include the port.</p>
     *
     * @param socketAddressHostname the socket address hostname string
     */
    public void setSocketAddressHostname(String socketAddressHostname) {
        this.socketAddressHostname = socketAddressHostname;
    }

    /**
     * Gets the unique id.
     *
     * @return the unique id
     */
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    /**
     * Sets the unique id.
     *
     * @param uniqueId the unique id
     */
    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    /**
     * Gets the profile properties.
     *
     * <p>This should be a valid JSON string.</p>
     *
     * @return the profile properties, as JSON
     */
    public String getPropertiesJson() {
        return this.propertiesJson;
    }

    /**
     * Sets the profile properties.
     *
     * <p>This should be a valid JSON string.</p>
     *
     * @param propertiesJson the profile properties, as JSON
     */
    public void setPropertiesJson(String propertiesJson) {
        this.propertiesJson = propertiesJson;
    }

    /**
     * Determines if authentication failed.
     *
     * <p>When {@code true}, the client connecting will be disconnected
     * with the {@link #getFailMessage() fail message}.</p>
     *
     * @return {@code true} if authentication failed, {@code false} otherwise
     */
    public boolean isFailed() {
        return this.failed;
    }

    /**
     * Sets if authentication failed and the client should be disconnected.
     *
     * <p>When {@code true}, the client connecting will be disconnected
     * with the {@link #getFailMessage() fail message}.</p>
     *
     * @param failed {@code true} if authentication failed, {@code false} otherwise
     */
    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    /**
     * Gets the message to display to the client when authentication fails.
     *
     * @return the message to display to the client
     */
    public String getFailMessage() {
        return this.failMessage;
    }

    /**
     * Sets the message to display to the client when authentication fails.
     *
     * @param failMessage the message to display to the client
     */
    public void setFailMessage(String failMessage) {
        Validate.notEmpty(failMessage, "fail message cannot be null or empty");
        this.failMessage = failMessage;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
