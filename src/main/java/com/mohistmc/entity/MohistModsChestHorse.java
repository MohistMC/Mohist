package com.mohistmc.entity;

import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftChestedHorse;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;

public class MohistModsChestHorse extends CraftChestedHorse {

    public MohistModsChestHorse(CraftServer server, AbstractChestedHorse entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftCustomChestHorse{" + entityName + '}';
    }

    @Override
    public AbstractChestedHorse getHandle() {
        return (AbstractChestedHorse) entity;
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
