package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.passive.FishEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Fish;

public class CraftFish extends CraftWaterMob implements Fish {

    public CraftFish(CraftServer server, FishEntity entity) {
        super(server, entity);
    }

    @Override
    public FishEntity getHandle() {
        return (FishEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftFish";
    }
}
