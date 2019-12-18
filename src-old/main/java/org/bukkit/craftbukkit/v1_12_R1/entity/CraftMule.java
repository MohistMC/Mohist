package org.bukkit.craftbukkit.v1_12_R1.entity;

import net.minecraft.entity.passive.EntityMule;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Mule;

public class CraftMule extends CraftChestedHorse implements Mule {

    public CraftMule(CraftServer server, EntityMule entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftMule";
    }

    @Override
    public EntityType getType() {
        return EntityType.MULE;
    }

    @Override
    public Variant getVariant() {
        return Variant.MULE;
    }
}
