package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.entity.passive.ParrotEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Parrot;

public class CraftParrot extends CraftTameableAnimal implements Parrot {

    public CraftParrot(CraftServer server, ParrotEntity parrot) {
        super(server, parrot);
    }

    @Override
    public ParrotEntity getHandle() {
        return (ParrotEntity) entity;
    }

    @Override
    public Variant getVariant() {
        return Variant.values()[getHandle().getVariant()];
    }

    @Override
    public void setVariant(Variant variant) {
        Preconditions.checkArgument(variant != null, "variant");

        getHandle().setVariant(variant.ordinal());
    }

    @Override
    public String toString() {
        return "CraftParrot";
    }

    @Override
    public EntityType getType() {
        return EntityType.PARROT;
    }
}
