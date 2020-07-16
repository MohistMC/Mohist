package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.mob.IllagerEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Illager;

public class CraftIllager extends CraftRaider implements Illager {

    public CraftIllager(CraftServer server, IllagerEntity entity) {
        super(server, entity);
    }

    @Override
    public IllagerEntity getHandle() {
        return (IllagerEntity) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftIllager";
    }
}
