package com.mohistmc.entity;

import net.minecraft.entity.passive.horse.AbstractChestedHorseEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftChestedHorse;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;

public class CraftCustomChestHorse extends CraftChestedHorse {

    public CraftCustomChestHorse(CraftServer server, AbstractChestedHorseEntity entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftCustomChestHorse";
    }

    @Override
    public EntityType getType() {
        EntityType type = EntityType.fromName(this.entityName);
        if (type != null) {
            return type;
        } else {
            return EntityType.UNKNOWN;
        }
    }

    @Override
    public Horse.Variant getVariant() {
        return Horse.Variant.FORGE_MOD_CHEST_HORSE;
    }
}
