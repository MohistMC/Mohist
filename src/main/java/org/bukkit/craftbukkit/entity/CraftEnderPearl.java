package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.thrown.ThrownEnderpearlEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;

public class CraftEnderPearl extends CraftThrowableProjectile implements EnderPearl {
    public CraftEnderPearl(CraftServer server, ThrownEnderpearlEntity entity) {
        super(server, entity);
    }

    @Override
    public ThrownEnderpearlEntity getHandle() {
        return (ThrownEnderpearlEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftEnderPearl";
    }

    @Override
    public EntityType getType() {
        return EntityType.ENDER_PEARL;
    }
}
