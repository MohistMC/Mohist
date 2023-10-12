package com.mohistmc.api.event;

import java.net.InetSocketAddress;
import java.util.Iterator;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftIconCache;
import org.bukkit.entity.Player;

public class MohistServerListPingEvent extends org.bukkit.event.server.ServerListPingEvent {

    private final Object[] players;
    public CraftIconCache icon;

    public MohistServerListPingEvent(Connection connection, MinecraftServer server) {
        super(connection.hostname, ((InetSocketAddress) connection.getRemoteAddress()).getAddress(), server.server.getMotd(), server.getPlayerList().getMaxPlayers());
        this.icon = ((CraftServer) Bukkit.getServer()).getServerIcon();
        this.players = server.getPlayerList().players.toArray();
    }

    public Object[] getPlayers() {
        return players;
    }

    @Override
    public void setServerIcon(org.bukkit.util.CachedServerIcon icon) {
        if (!(icon instanceof CraftIconCache)) {
            throw new IllegalArgumentException(icon + " was not created by " + org.bukkit.craftbukkit.v1_20_R1.CraftServer.class);
        }
        this.icon = (CraftIconCache) icon;
    }

    @Override
    public Iterator<Player> iterator() throws UnsupportedOperationException {
        return new Iterator<>() {
            int i;
            int ret = Integer.MIN_VALUE;
            ServerPlayer player;

            @Override
            public boolean hasNext() {
                if (player != null) {
                    return true;
                }
                final Object[] currentPlayers = players;
                for (int length = currentPlayers.length, i = this.i; i < length; i++) {
                    final ServerPlayer player = (ServerPlayer) currentPlayers[i];
                    if (player != null) {
                        this.i = i + 1;
                        this.player = player;
                        return true;
                    }
                }
                return false;
            }

            @Override
            public Player next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                final ServerPlayer player = this.player;
                this.player = null;
                this.ret = this.i - 1;
                return player.getBukkitEntity();
            }

            @Override
            public void remove() {
                final Object[] currentPlayers = players;
                final int i = this.ret;
                if (i < 0 || currentPlayers[i] == null) {
                    throw new IllegalStateException();
                }
                currentPlayers[i] = null;
            }
        };
    }
}
