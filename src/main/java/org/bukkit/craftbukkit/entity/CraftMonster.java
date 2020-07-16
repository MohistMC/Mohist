package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.mob.HostileEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Monster;

public class CraftMonster extends CraftCreature implements Monster {

    public CraftMonster(CraftServer server, HostileEntity entity) {
        super(server, entity);
    }

    @Override
    public HostileEntity getHandle() {
        return (HostileEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftMonster";
    }
}
