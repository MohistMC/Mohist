package org.bukkit.craftbukkit.v1_16_R3.util;

import java.util.HashSet;
import java.util.List;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import org.bukkit.entity.Player;

public class LazyPlayerSet extends LazyHashSet<Player> {

    private final MinecraftServer server;

    public LazyPlayerSet(MinecraftServer server) {
        this.server = server;
    }

    @Override
    protected HashSet<Player> makeReference() { // Paper - protected
        if (reference != null) {
            throw new IllegalStateException("Reference already created!");
        }
        List<ServerPlayerEntity> players = server.getPlayerList().players;
        // Paper start
        return makePlayerSet(this.server);
    }

    public static HashSet<Player> makePlayerSet(final MinecraftServer server) {
        // Paper end
        List<ServerPlayerEntity> players = server.getPlayerList().players;
        HashSet<Player> reference = new HashSet<Player>(players.size());
        for (ServerPlayerEntity player : players) {
            reference.add(player.getBukkitEntity());
        }
        return reference;
    }
}
