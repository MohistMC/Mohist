package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LightningStrike;

public class CraftLightningStrike extends CraftEntity implements LightningStrike {
    public CraftLightningStrike(final CraftServer server, final net.minecraft.world.entity.LightningBolt entity) {
        super(server, entity);
    }

    @Override
    public boolean isEffect() {
        return getHandle().isEffect;
    }

    @Override
    public net.minecraft.world.entity.LightningBolt getHandle() {
        return (net.minecraft.world.entity.LightningBolt) entity;
    }

    @Override
    public String toString() {
        return "CraftLightningStrike";
    }

    @Override
    public EntityType getType() {
        return EntityType.LIGHTNING;
    }
}
