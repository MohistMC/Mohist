package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.monster.MagmaCubeEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MagmaCube;

public class CraftMagmaCube extends CraftSlime implements MagmaCube {

    public CraftMagmaCube(CraftServer server, MagmaCubeEntity entity) {
        super(server, entity);
    }

    @Override
    public MagmaCubeEntity getHandle() {
        return (MagmaCubeEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftMagmaCube";
    }

    @Override
    public EntityType getType() {
        return EntityType.MAGMA_CUBE;
    }
}
