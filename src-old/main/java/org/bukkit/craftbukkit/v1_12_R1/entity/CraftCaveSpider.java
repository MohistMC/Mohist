package org.bukkit.craftbukkit.v1_12_R1.entity;

import net.minecraft.entity.monster.EntityCaveSpider;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.EntityType;

public class CraftCaveSpider extends CraftSpider implements CaveSpider {
    public CraftCaveSpider(CraftServer server, EntityCaveSpider entity) {
        super(server, entity);
    }

    @Override
    public EntityCaveSpider getHandle() {
        return (EntityCaveSpider) entity;
    }

    @Override
    public String toString() {
        return "CraftCaveSpider";
    }

    public EntityType getType() {
        return EntityType.CAVE_SPIDER;
    }
}
