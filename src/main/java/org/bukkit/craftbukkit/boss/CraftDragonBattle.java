package org.bukkit.craftbukkit.boss;

import com.google.common.base.Preconditions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.boss.dragon.EnderDragonSpawnState;
import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.DragonBattle;
import org.bukkit.entity.EnderDragon;

public class CraftDragonBattle implements DragonBattle {

    private final EnderDragonFight handle;

    public CraftDragonBattle(EnderDragonFight handle) {
        this.handle = handle;
    }

    @Override
    public EnderDragon getEnderDragon() {
        Entity entity = handle.world.getEntity(handle.dragonUuid);
        return (entity != null) ? (EnderDragon) entity.getBukkitEntity() : null;
    }

    @Override
    public BossBar getBossBar() {
        return new CraftBossBar(handle.bossBar);
    }

    @Override
    public Location getEndPortalLocation() {
        return new Location(handle.world.getCraftWorld(), handle.exitPortalLocation.getX(), handle.exitPortalLocation.getY(), handle.exitPortalLocation.getZ());
    }

    @Override
    public boolean hasBeenPreviouslyKilled() {
        return handle.hasPreviouslyKilled(); // PAIL rename hasBeenPreviouslyKilled
    }

    @Override
    public void initiateRespawn() {
        this.handle.respawnDragon(); // PAIL rename initiateRespawn
    }

    @Override
    public RespawnPhase getRespawnPhase() {
        return toBukkitRespawnPhase(handle.dragonSpawnState);
    }

    @Override
    public boolean setRespawnPhase(RespawnPhase phase) {
        Preconditions.checkArgument(phase != null && phase != RespawnPhase.NONE, "Invalid respawn phase provided: %s", phase);

        if (handle.dragonSpawnState == null) {
            return false;
        }

        this.handle.setSpawnState(toNMSRespawnPhase(phase));
        return true;
    }

    @Override
    public void resetCrystals() {
        this.handle.resetEndCrystals(); // PAIL rename resetCrystals
    }

    @Override
    public int hashCode() {
        return handle.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CraftDragonBattle && ((CraftDragonBattle) obj).handle == this.handle;
    }

    private RespawnPhase toBukkitRespawnPhase(EnderDragonSpawnState phase) {
        return (phase != null) ? RespawnPhase.values()[phase.ordinal()] : RespawnPhase.NONE;
    }

    private EnderDragonSpawnState toNMSRespawnPhase(RespawnPhase phase) {
        return (phase != RespawnPhase.NONE) ? EnderDragonSpawnState.values()[phase.ordinal()] : null;
    }
}
