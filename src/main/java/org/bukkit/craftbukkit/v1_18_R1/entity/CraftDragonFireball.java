package org.bukkit.craftbukkit.v1_18_R1.entity;

import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.EntityType;

public class CraftDragonFireball extends CraftFireball implements DragonFireball {
    public CraftDragonFireball(CraftServer server, net.minecraft.world.entity.projectile.DragonFireball entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftDragonFireball";
    }

    @Override
    public EntityType getType() {
        return EntityType.DRAGON_FIREBALL;
    }
}
