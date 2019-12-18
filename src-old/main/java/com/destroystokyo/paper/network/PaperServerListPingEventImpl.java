package com.destroystokyo.paper.network;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import org.bukkit.entity.Player;
import org.bukkit.util.CachedServerIcon;

class PaperServerListPingEventImpl extends PaperServerListPingEvent {

    private final MinecraftServer server;

    PaperServerListPingEventImpl(MinecraftServer server, StatusClient client, int protocolVersion, @Nullable CachedServerIcon icon) {
        super(client, server.getMOTD(), server.getCurrentPlayerCount(), server.getMaxPlayers(),
                server.getServerModName() + ' ' + server.getMinecraftVersion(), protocolVersion, icon);
        this.server = server;
    }

    @Override
    protected final Object[] getOnlinePlayers() {
        return this.server.getPlayerList().playerEntityList.toArray();
    }

    @Override
    protected final Player getBukkitPlayer(Object player) {
        return ((EntityPlayerMP) player).getBukkitEntity();
    }

}
