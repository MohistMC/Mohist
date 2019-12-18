package org.bukkit.craftbukkit.v1_12_R1.entity;

import net.minecraft.entity.item.EntityExpBottle;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ThrownExpBottle;

public class CraftThrownExpBottle extends CraftProjectile implements ThrownExpBottle {
    public CraftThrownExpBottle(CraftServer server, EntityExpBottle entity) {
        super(server, entity);
    }

    @Override
    public EntityExpBottle getHandle() {
        return (EntityExpBottle) entity;
    }

    @Override
    public String toString() {
        return "EntityThrownExpBottle";
    }

    public EntityType getType() {
        return EntityType.THROWN_EXP_BOTTLE;
    }
}
