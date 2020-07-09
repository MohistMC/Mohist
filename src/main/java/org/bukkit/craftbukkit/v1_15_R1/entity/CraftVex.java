package org.bukkit.craftbukkit.v1_15_R1.entity;

import net.minecraft.entity.monster.VexEntity;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Vex;

public class CraftVex extends CraftMonster implements Vex {

    public CraftVex(CraftServer server, VexEntity entity) {
        super(server, entity);
    }

    @Override
    public VexEntity getHandle() {
        return (VexEntity) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftVex";
    }

    @Override
    public EntityType getType() {
        return EntityType.VEX;
    }

    @Override
    public boolean isCharging() {
        return getHandle().isCharging();
    }

    @Override
    public void setCharging(boolean charging) {
        getHandle().setCharging(charging);
    }
}
