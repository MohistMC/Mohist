package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.mob.WaterCreatureEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.WaterMob;

public class CraftWaterMob extends CraftCreature implements WaterMob {

    public CraftWaterMob(CraftServer server, WaterCreatureEntity entity) {
        super(server, entity);
    }

    @Override
    public WaterCreatureEntity getHandle() {
        return (WaterCreatureEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftWaterMob";
    }
}
