package org.bukkit.craftbukkit.v1_15_R1.entity;

import net.minecraft.entity.projectile.TridentEntity;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Trident;

public class CraftTrident extends CraftArrow implements Trident {

    public CraftTrident(CraftServer server, TridentEntity entity) {
        super(server, entity);
    }

    @Override
    public TridentEntity getHandle() {
        return (TridentEntity) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftTrident";
    }

    @Override
    public EntityType getType() {
        return EntityType.TRIDENT;
    }
}
