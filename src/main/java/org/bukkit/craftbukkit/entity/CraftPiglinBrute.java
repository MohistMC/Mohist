package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.monster.piglin.PiglinBruteEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PiglinBrute;

public class CraftPiglinBrute extends CraftPiglinAbstract implements PiglinBrute {

    public CraftPiglinBrute(CraftServer server, PiglinBruteEntity entity) {
        super(server, entity);
    }

    @Override
    public PiglinBruteEntity getHandle() {
        return (PiglinBruteEntity) super.getHandle();
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
