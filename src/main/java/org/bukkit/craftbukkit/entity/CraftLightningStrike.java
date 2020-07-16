package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.LightningEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LightningStrike;

public class CraftLightningStrike extends CraftEntity implements LightningStrike {
    public CraftLightningStrike(final CraftServer server, final LightningEntity entity) {
        super(server, entity);
    }

    @Override
    public boolean isEffect() {
        return ((LightningEntity) super.getHandle()).isEffect;
    }

    @Override
    public LightningEntity getHandle() {
        return (LightningEntity) entity;
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
