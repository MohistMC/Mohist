package org.bukkit.craftbukkit.v1_12_R1.entity;

import net.minecraft.entity.monster.EntitySilverfish;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Silverfish;

public class CraftSilverfish extends CraftMonster implements Silverfish {
    public CraftSilverfish(CraftServer server, EntitySilverfish entity) {
        super(server, entity);
    }

    @Override
    public EntitySilverfish getHandle() {
        return (EntitySilverfish) entity;
    }

    @Override
    public String toString() {
        return "CraftSilverfish";
    }

    public EntityType getType() {
        return EntityType.SILVERFISH;
    }
}
