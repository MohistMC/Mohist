package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.monster.AbstractIllagerEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Illager;

public class CraftIllager extends CraftRaider implements Illager {

    public CraftIllager(CraftServer server, AbstractIllagerEntity entity) {
        super(server, entity);
    }

    @Override
    public AbstractIllagerEntity getHandle() {
        return (AbstractIllagerEntity) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftIllager";
    }
}
