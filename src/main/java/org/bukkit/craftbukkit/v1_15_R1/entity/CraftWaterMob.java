package org.bukkit.craftbukkit.v1_15_R1.entity;

import net.minecraft.entity.passive.WaterMobEntity;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.entity.WaterMob;

public class CraftWaterMob extends CraftCreature implements WaterMob {

    public CraftWaterMob(CraftServer server, WaterMobEntity entity) {
        super(server, entity);
    }

    @Override
    public WaterMobEntity getHandle() {
        return (WaterMobEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftWaterMob";
    }
}
