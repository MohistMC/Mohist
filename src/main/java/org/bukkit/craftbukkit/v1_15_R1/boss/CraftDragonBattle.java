package org.bukkit.craftbukkit.v1_15_R1.boss;

import com.google.common.base.Preconditions;
import net.minecraft.entity.Entity;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.world.end.DragonSpawnState;
import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.DragonBattle;
import org.bukkit.entity.EnderDragon;

public class CraftDragonBattle implements DragonBattle {

    private final DragonFightManager handle;

    public CraftDragonBattle(DragonFightManager handle) {
        this.handle = handle;
    }

    @Override
    public EnderDragon getEnderDragon() {
        Entity entity = handle.world.getEntityByUuid(handle.dragonUniqueId);
        return (entity != null) ? (EnderDragon) entity.getBukkitEntity() : null;
    }

    @Override
    public BossBar getBossBar() {
        return new CraftBossBar(handle.bossInfo);
    }

    @Override
    public Location getEndPortalLocation() {
        return new Location(handle.world.getWorldCB(), handle.exitPortalLocation.getX(), handle.exitPortalLocation.getY(), handle.exitPortalLocation.getZ());
    }

    @Override
    public boolean hasBeenPreviouslyKilled() {
        return handle.hasPreviouslyKilledDragon(); // PAIL rename hasBeenPreviouslyKilled
    }

    @Override
    public void initiateRespawn() {
        this.handle.tryRespawnDragon(); // PAIL rename initiateRespawn
    }

    @Override
    public RespawnPhase getRespawnPhase() {
        return toBukkitRespawnPhase(handle.respawnState);
    }

    @Override
    public boolean setRespawnPhase(RespawnPhase phase) {
        Preconditions.checkArgument(phase != null && phase != RespawnPhase.NONE, "Invalid respawn phase provided: %s", phase);

        if (handle.respawnState == null) {
            return false;
        }

        this.handle.setRespawnState(toNMSRespawnPhase(phase));
        return true;
    }

    @Override
    public void resetCrystals() {
        this.handle.resetSpikeCrystals(); // PAIL rename resetCrystals
    }

    @Override
    public int hashCode() {
        return handle.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CraftDragonBattle && ((CraftDragonBattle) obj).handle == this.handle;
    }

    private RespawnPhase toBukkitRespawnPhase(DragonSpawnState phase) {
        return (phase != null) ? RespawnPhase.values()[phase.ordinal()] : RespawnPhase.NONE;
    }

    private DragonSpawnState toNMSRespawnPhase(RespawnPhase phase) {
        return (phase != RespawnPhase.NONE) ? DragonSpawnState.values()[phase.ordinal()] : null;
    }
}
