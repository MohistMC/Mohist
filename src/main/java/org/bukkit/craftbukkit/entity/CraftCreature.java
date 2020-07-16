package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.mob.MobEntityWithAi;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Creature;

public class CraftCreature extends CraftMob implements Creature {
    public CraftCreature(CraftServer server, MobEntityWithAi entity) {
        super(server, entity);
    }

    @Override
    public MobEntityWithAi getHandle() {
        return (MobEntityWithAi) entity;
    }

    @Override
    public String toString() {
        return "CraftCreature";
    }
}
