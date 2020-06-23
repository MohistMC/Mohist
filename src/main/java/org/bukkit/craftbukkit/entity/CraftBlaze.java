package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.monster.BlazeEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.EntityType;

public class CraftBlaze extends CraftMonster implements Blaze {
    public CraftBlaze(CraftServer server, BlazeEntity entity) {
        super(server, entity);
    }

    @Override
    public BlazeEntity getHandle() {
        return (BlazeEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftBlaze";
    }

    @Override
    public EntityType getType() {
        return EntityType.BLAZE;
    }
}
