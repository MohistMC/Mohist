package org.bukkit.craftbukkit.v1_15_R1.entity;

import net.minecraft.entity.LivingEntity;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.entity.ComplexLivingEntity;

public abstract class CraftComplexLivingEntity extends CraftLivingEntity implements ComplexLivingEntity {
    public CraftComplexLivingEntity(CraftServer server, LivingEntity entity) {
        super(server, entity);
    }

    @Override
    public LivingEntity getHandle() {
        return (LivingEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftComplexLivingEntity";
    }
}
