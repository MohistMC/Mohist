package org.bukkit.craftbukkit.v1_18_R2.entity;

import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.EntityType;

public class CraftElderGuardian extends CraftGuardian implements ElderGuardian {

    public CraftElderGuardian(CraftServer server, net.minecraft.world.entity.monster.ElderGuardian entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftElderGuardian";
    }

    @Override
    public EntityType getType() {
        return EntityType.ELDER_GUARDIAN;
    }

    @Override
    public boolean isElder() {
        return true;
    }
}
