package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.monster.SilverfishEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Silverfish;

public class CraftSilverfish extends CraftMonster implements Silverfish {
    public CraftSilverfish(CraftServer server, SilverfishEntity entity) {
        super(server, entity);
    }

    @Override
    public SilverfishEntity getHandle() {
        return (SilverfishEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftSilverfish";
    }

    @Override
    public EntityType getType() {
        return EntityType.SILVERFISH;
    }
}
