package org.bukkit.craftbukkit.v1_12_R1.entity;

import net.minecraft.entity.projectile.EntitySpectralArrow;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.SpectralArrow;

public class CraftSpectralArrow extends CraftArrow implements SpectralArrow {

    public CraftSpectralArrow(CraftServer server, EntitySpectralArrow entity) {
        super(server, entity);
    }

    @Override
    public EntitySpectralArrow getHandle() {
        return (EntitySpectralArrow) entity;
    }

    @Override
    public String toString() {
        return "CraftSpectralArrow";
    }

    @Override
    public EntityType getType() {
        return EntityType.SPECTRAL_ARROW;
    }

    @Override
    public int getGlowingTicks() {
        return getHandle().duration;
    }

    @Override
    public void setGlowingTicks(int duration) {
        getHandle().duration = duration;
    }
}
