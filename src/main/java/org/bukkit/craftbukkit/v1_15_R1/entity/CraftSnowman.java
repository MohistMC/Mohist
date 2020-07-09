package org.bukkit.craftbukkit.v1_15_R1.entity;

import net.minecraft.entity.passive.SnowGolemEntity;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowman;

public class CraftSnowman extends CraftGolem implements Snowman {
    public CraftSnowman(CraftServer server, SnowGolemEntity entity) {
        super(server, entity);
    }

    @Override
    public boolean isDerp() {
        return !getHandle().isPumpkinEquipped();
    }

    @Override
    public void setDerp(boolean derpMode) {
        getHandle().setPumpkinEquipped(!derpMode);
    }

    @Override
    public SnowGolemEntity getHandle() {
        return (SnowGolemEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftSnowman";
    }

    @Override
    public EntityType getType() {
        return EntityType.SNOWMAN;
    }
}
