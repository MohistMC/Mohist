package org.bukkit.craftbukkit.v1_12_R1.entity;

import net.minecraft.entity.monster.EntityMagmaCube;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MagmaCube;

public class CraftMagmaCube extends CraftSlime implements MagmaCube {

    public CraftMagmaCube(CraftServer server, EntityMagmaCube entity) {
        super(server, entity);
    }

    public EntityMagmaCube getHandle() {
        return (EntityMagmaCube) entity;
    }

    @Override
    public String toString() {
        return "CraftMagmaCube";
    }

    public EntityType getType() {
        return EntityType.MAGMA_CUBE;
    }
}
