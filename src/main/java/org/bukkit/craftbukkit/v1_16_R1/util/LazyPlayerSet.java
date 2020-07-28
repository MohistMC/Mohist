package org.bukkit.craftbukkit.v1_16_R1.util;

import java.util.HashSet;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.bukkit.entity.Player;
import red.mohist.extra.entity.ExtraEntity;

public class LazyPlayerSet extends LazyHashSet<Player> {

    private final MinecraftServer server;

    public LazyPlayerSet(MinecraftServer server) {
        this.server = server;
    }

    @Override
    HashSet<Player> makeReference() {
        if (reference != null)
            throw new IllegalStateException("Reference already created");
        List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
        HashSet<Player> reference = new HashSet<Player>(players.size());
        for (ServerPlayerEntity player : players)
            reference.add((Player) ((ExtraEntity)player).getBukkitEntity());

        return reference;
    }
}
