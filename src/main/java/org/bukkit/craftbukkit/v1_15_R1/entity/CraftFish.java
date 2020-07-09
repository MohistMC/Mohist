package org.bukkit.craftbukkit.v1_15_R1.entity;

import net.minecraft.entity.passive.fish.AbstractFishEntity;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.entity.Fish;

public class CraftFish extends CraftWaterMob implements Fish {

    public CraftFish(CraftServer server, AbstractFishEntity entity) {
        super(server, entity);
    }

    @Override
    public AbstractFishEntity getHandle() {
        return (AbstractFishEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftFish";
    }
}
