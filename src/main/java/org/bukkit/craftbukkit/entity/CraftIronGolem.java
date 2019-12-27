package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.passive.IronGolemEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;

public class CraftIronGolem extends CraftGolem implements IronGolem {
    public CraftIronGolem(CraftServer server, IronGolemEntity entity) {
        super(server, entity);
    }

    @Override
    public IronGolemEntity getHandle() {
        return (IronGolemEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftIronGolem";
    }

    @Override
    public boolean isPlayerCreated() {
        return getHandle().isPlayerCreated();
    }

    @Override
    public void setPlayerCreated(boolean playerCreated) {
        getHandle().setPlayerCreated(playerCreated);
    }

    @Override
    public EntityType getType() {
        return EntityType.IRON_GOLEM;
    }
}
