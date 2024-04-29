package com.mohistmc.mohist.bukkit.entity;

import net.minecraft.world.entity.raid.Raider;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftRaider;
import org.bukkit.entity.EntityCategory;

public class MohistModsRaider extends CraftRaider {

    public MohistModsRaider(CraftServer server, Raider entity) {
        super(server, entity);
    }

    @Override
    public Raider getHandle() {
        return (Raider) this.entity;
    }

    @Override
    public String toString() {
        return "MohistModsRaider{" + getType() + '}';
    }

    @Override
    public EntityCategory getCategory() {
        return EntityCategory.ILLAGER;
    }
}
