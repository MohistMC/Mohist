package com.mohistmc.mohist.bukkit.entity;

import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftAbstractHorse;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Variant;

public class MohistModsAbstractHorse extends CraftAbstractHorse {

    public MohistModsAbstractHorse(CraftServer server, AbstractHorse entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "MohistModsAbstractHorse{" + getType() + '}';
    }

    @Override
    public Horse.Variant getVariant() {
        //  Horse.Variant.FORGE_MOD_HORSE;
        return Variant.HORSE;
    }

    @Override
    public EntityCategory getCategory() {
        return EntityCategory.NONE;
    }
}
