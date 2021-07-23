package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PiglinBrute;

public class CraftPiglinBrute extends CraftPiglinAbstract implements PiglinBrute {

    public CraftPiglinBrute(CraftServer server, net.minecraft.world.entity.monster.piglin.PiglinBrute entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.piglin.PiglinBrute getHandle() {
        return (net.minecraft.world.entity.monster.piglin.PiglinBrute) super.getHandle();
    }

    @Override
    public EntityType getType() {
        return EntityType.PIGLIN_BRUTE;
    }

    @Override
    public String toString() {
        return "CraftPiglinBrute";
    }
}
