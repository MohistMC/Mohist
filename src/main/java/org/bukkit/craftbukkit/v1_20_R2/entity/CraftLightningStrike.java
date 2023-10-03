package org.bukkit.craftbukkit.v1_20_R2.entity;

import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;

public class CraftLightningStrike extends CraftEntity implements LightningStrike {
    public CraftLightningStrike(final CraftServer server, final net.minecraft.world.entity.LightningBolt entity) {
        super(server, entity);
    }

    @Override
    public boolean isEffect() {
        return getHandle().visualOnly;
    }

    public int getFlashes() {
        return getHandle().flashes;
    }

    public void setFlashes(int flashes) {
        getHandle().flashes = flashes;
    }

    public int getLifeTicks() {
        return getHandle().life;
    }

    public void setLifeTicks(int ticks) {
        getHandle().life = ticks;
    }

    public Player getCausingPlayer() {
        ServerPlayer player = getHandle().getCause();
        return (player != null) ? player.getBukkitEntity() : null;
    }

    public void setCausingPlayer(Player player) {
        getHandle().setCause((player != null) ? ((CraftPlayer) player).getHandle() : null);
    }

    @Override
    public net.minecraft.world.entity.LightningBolt getHandle() {
        return (net.minecraft.world.entity.LightningBolt) entity;
    }

    @Override
    public String toString() {
        return "CraftLightningStrike";
    }

    // Spigot start
    private final LightningStrike.Spigot spigot = new LightningStrike.Spigot() {

        @Override
        public boolean isSilent()
        {
            return getHandle().isSilent;
        }
    };

    @Override
    public LightningStrike.Spigot spigot() {
        return spigot;
    }
    // Spigot end
}
