package com.mohistmc.entity;

import net.minecraft.entity.passive.AbstractHorse;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;

public class CraftCustomAbstractHorse extends CraftAbstractHorse {

    public CraftCustomAbstractHorse(CraftServer server, AbstractHorse entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftCustomAbstractHorse";
    }

    @Override
    public EntityType getType() {
        EntityType type = EntityType.fromName(this.entityName);
        if (type != null) {
            return type;
        } else {
            return EntityType.FORGE_MOD;
        }
    }

    @Override
    public Horse.Variant getVariant() {
        return Horse.Variant.FORGE_MOD_HORSE;
    }
}
