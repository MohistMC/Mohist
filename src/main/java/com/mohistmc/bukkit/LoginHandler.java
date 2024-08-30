package com.mohistmc.bukkit;

import com.mohistmc.MohistConfig;
import java.util.concurrent.ExecutionException;
import net.minecraft.network.chat.Component;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.bukkit.craftbukkit.v1_20_R1.util.Waitable;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent.Result;

public class LoginHandler {

    public void fireEvents(ServerLoginPacketListenerImpl serverLoginPacketListener) throws Exception {
        // Paper start - Velocity support
        if (serverLoginPacketListener.velocityLoginMessageId == -1 && MohistConfig.velocity_enabled) {
            serverLoginPacketListener.disconnect("This server requires you to connect with Velocity.");
            return;
        }
        // Paper end
        String playerName = serverLoginPacketListener.gameProfile.getName();
        java.net.InetAddress address = ((java.net.InetSocketAddress) serverLoginPacketListener.connection.getRemoteAddress()).getAddress();
        java.util.UUID uniqueId = serverLoginPacketListener.gameProfile.getId();
        final org.bukkit.craftbukkit.v1_20_R1.CraftServer server = serverLoginPacketListener.server.server;

        AsyncPlayerPreLoginEvent asyncEvent = new AsyncPlayerPreLoginEvent(playerName, address, uniqueId);
        server.getPluginManager().callEvent(asyncEvent);

        if (PlayerPreLoginEvent.getHandlerList().getRegisteredListeners().length != 0) {
            final PlayerPreLoginEvent event = new PlayerPreLoginEvent(playerName, address, uniqueId);
            if (asyncEvent.getResult() != PlayerPreLoginEvent.Result.ALLOWED) {
                event.disallow(asyncEvent.getResult(), asyncEvent.getKickMessage());
            }
            Waitable<Result> waitable = new Waitable<>() {
                @Override
                protected PlayerPreLoginEvent.Result evaluate() {
                    server.getPluginManager().callEvent(event);
                    return event.getResult();
                }
            };

            serverLoginPacketListener.server.processQueue.add(waitable);
            if (waitable.get() != PlayerPreLoginEvent.Result.ALLOWED) {
                serverLoginPacketListener.disconnect(Component.nullToEmpty(event.getKickMessage()));
                return;
            }
        } else {
            if (asyncEvent.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
                serverLoginPacketListener.disconnect(Component.nullToEmpty(asyncEvent.getKickMessage()));
                return;
            }
        }
        // CraftBukkit end
        serverLoginPacketListener.state = ServerLoginPacketListenerImpl.State.NEGOTIATING; // FORGE: continue NEGOTIATING, we move to READY_TO_ACCEPT after Forge is ready
    }

    public static void disconnect(ServerGamePacketListenerImpl serverGamePacketListener, String pTextComponent){
        Waitable waitable = new Waitable() {
            @Override
            protected Object evaluate() {
                serverGamePacketListener.disconnect(pTextComponent);
                return null;
            }
        };

        serverGamePacketListener.server.processQueue.add(waitable);

        try {
            waitable.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
