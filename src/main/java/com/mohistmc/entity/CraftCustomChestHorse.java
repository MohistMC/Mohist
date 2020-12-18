package com.mohistmc.entity;

import net.minecraft.entity.passive.horse.AbstractChestedHorseEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftChestedHorse;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;

public class CraftCustomChestHorse extends CraftChestedHorse {

    public CraftCustomChestHorse(CraftServer server, AbstractChestedHorseEntity entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftCustomChestHorse{" + entityName + '}';
    }

    @Override
    public EntityType getType() {
        EntityType type = EntityType.fromName(this.entityName);
        if (type != null) {
            return type;
        } else {
            return EntityType.FORGE_MOD_CHEST_HORSE;
        }
    }

    @Override
    public Horse.Variant getVariant() {
        return Horse.Variant.FORGE_MOD_CHEST_HORSE;
    }

    @Override
    public EntityCategory getCategory() {
        return EntityCategory.NONE;
    }
}
