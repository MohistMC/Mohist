package org.bukkit.event.server;

import java.net.InetAddress;
import java.util.Iterator;

import org.apache.commons.lang.Validate;
import org.bukkit.UndefinedNullability;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.util.CachedServerIcon;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a server list ping is coming in. Displayed players can be
 * checked and removed by {@link #iterator() iterating} over this event.
 */
public class ServerListPingEvent extends ServerEvent implements Iterable<Player> {
    private static final int MAGIC_PLAYER_COUNT = Integer.MIN_VALUE;
    private static final HandlerList handlers = new HandlerList();
    private final InetAddress address;
    private net.kyori.adventure.text.Component motd; // Paper
    private final int numPlayers;
    private int maxPlayers;

    @Deprecated // Paper
    public ServerListPingEvent(@NotNull final InetAddress address, @NotNull final String motd, final int numPlayers, final int maxPlayers) {
        super(true);
        Validate.isTrue(numPlayers >= 0, "Cannot have negative number of players online", numPlayers);
        this.address = address;
        this.motd = org.bukkit.Bukkit.getUnsafe().legacyComponentSerializer().deserialize(motd); // Paper
        this.numPlayers = numPlayers;
        this.maxPlayers = maxPlayers;
    }

    /**
     * This constructor is intended for implementations that provide the
     * {@link #iterator()} method, thus provided the {@link #getNumPlayers()}
     * count.
     *
     * @param address    the address of the pinger
     * @param motd       the message of the day
     * @param maxPlayers the max number of players
     * @deprecated in favour of {@link #ServerListPingEvent(java.net.InetAddress, net.kyori.adventure.text.Component, int)}
     */
    @Deprecated // Paper
    protected ServerListPingEvent(@NotNull final InetAddress address, @NotNull final String motd, final int maxPlayers) {
        super(true);
        this.numPlayers = MAGIC_PLAYER_COUNT;
        this.address = address;
        this.motd = org.bukkit.Bukkit.getUnsafe().legacyComponentSerializer().deserialize(motd); // Paper
        this.maxPlayers = maxPlayers;
    }

    // Paper start
    public ServerListPingEvent(@NotNull final InetAddress address, @NotNull final net.kyori.adventure.text.Component motd, final int numPlayers, final int maxPlayers) {
        super(true);
        Validate.isTrue(numPlayers >= 0, "Cannot have negative number of players online", numPlayers);
        this.address = address;
        this.motd = motd;
        this.numPlayers = numPlayers;
        this.maxPlayers = maxPlayers;
    }

    /**
     * This constructor is intended for implementations that provide the
     * {@link #iterator()} method, thus provided the {@link #getNumPlayers()}
     * count.
     *
     * @param address    the address of the pinger
     * @param motd       the message of the day
     * @param maxPlayers the max number of players
     */
    protected ServerListPingEvent(@NotNull final InetAddress address, @NotNull final net.kyori.adventure.text.Component motd, final int maxPlayers) {
        super(true);
        this.numPlayers = MAGIC_PLAYER_COUNT;
        this.address = address;
        this.motd = motd;
        this.maxPlayers = maxPlayers;
    }

    /**
     * Get the message of the day message.
     *
     * @return the message of the day
     */
    public @NotNull net.kyori.adventure.text.Component motd() {
        return motd;
    }

    /**
     * Change the message of the day message.
     *
     * @param motd the message of the day
     */
    public void motd(@NotNull net.kyori.adventure.text.Component motd) {
        this.motd = motd;
    }
    // Paper end

    /**
     * Get the address the ping is coming from.
     *
     * @return the address
     */
    @NotNull
    public InetAddress getAddress() {
        return address;
    }

    /**
     * Get the message of the day message.
     *
     * @return the message of the day
     * @deprecated in favour of {@link #motd()}
     */
    @NotNull
    @Deprecated // Paper
    public String getMotd() {
        return org.bukkit.Bukkit.getUnsafe().legacyComponentSerializer().serialize(this.motd); // Paper
    }

    /**
     * Change the message of the day message.
     *
     * @param motd the message of the day
     * @deprecated in favour of {@link #motd(net.kyori.adventure.text.Component)}
     */
    @Deprecated // Paper
    public void setMotd(@NotNull String motd) {
        this.motd = org.bukkit.Bukkit.getUnsafe().legacyComponentSerializer().deserialize(motd); // Paper
    }

    /**
     * Get the number of players sent.
     *
     * @return the number of players
     */
    public int getNumPlayers() {
        int numPlayers = this.numPlayers;
        if (numPlayers == MAGIC_PLAYER_COUNT) {
            numPlayers = 0;
            for (@SuppressWarnings("unused") final Player player : this) {
                numPlayers++;
            }
        }
        return numPlayers;
    }

    /**
     * Get the maximum number of players sent.
     *
     * @return the maximum number of players
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Set the maximum number of players sent.
     *
     * @param maxPlayers the maximum number of player
     */
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    /**
     * Sets the server-icon sent to the client.
     *
     * @param icon the icon to send to the client
     * @throws IllegalArgumentException      if the {@link CachedServerIcon} is not
     *                                       created by the caller of this event; null may be accepted for some
     *                                       implementations
     * @throws UnsupportedOperationException if the caller of this event does
     *                                       not support setting the server icon
     */
    public void setServerIcon(@UndefinedNullability("implementation dependent") CachedServerIcon icon) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
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

    /**
     * {@inheritDoc}
     * <p>
     * Calling the {@link Iterator#remove()} method will force that particular
     * player to not be displayed on the player list, decrease the size
     * returned by {@link #getNumPlayers()}, and will not be returned again by
     * any new iterator.
     *
     * @throws UnsupportedOperationException if the caller of this event does
     *                                       not support removing players
     */
    @NotNull
    @Override
    public Iterator<Player> iterator() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
