package org.bukkit.craftbukkit.boss;

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
        if (handle.exitPortalLocation == null) {
            return null;
        }

        return new Location(handle.world.getCBWorld(), handle.exitPortalLocation.getX(), handle.exitPortalLocation.getY(), handle.exitPortalLocation.getZ());
    }

    @Override
    public boolean generateEndPortal(boolean withPortals) {
        if (handle.exitPortalLocation != null || handle.findExitPortal() != null) {
            return false;
        }

        this.handle.generatePortal(withPortals);
        return true;
    }

    @Override
    public boolean hasBeenPreviouslyKilled() {
        return handle.hasPreviouslyKilledDragon();
    }

    @Override
    public void initiateRespawn() {
        this.handle.tryRespawnDragon();
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
        this.handle.resetSpikeCrystals();
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
