package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.monster.EndermiteEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.EntityType;

public class CraftEndermite extends CraftMonster implements Endermite {

    public CraftEndermite(CraftServer server, EndermiteEntity entity) {
        super(server, entity);
    }

    @Override
    public EndermiteEntity getHandle() {
        return (EndermiteEntity) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftEndermite";
    }

    @Override
    public EntityType getType() {
        return EntityType.ENDERMITE;
    }

    @Override
    public boolean isPlayerSpawned() {
        return getHandle().isSpawnedByPlayer();
    }

    @Override
    public void setPlayerSpawned(boolean playerSpawned) {
        getHandle().setSpawnedByPlayer(playerSpawned);
    }
}
