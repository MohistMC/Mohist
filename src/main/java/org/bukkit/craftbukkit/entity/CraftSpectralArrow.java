package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.projectile.SpectralArrowEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.SpectralArrow;

public class CraftSpectralArrow extends CraftArrow implements SpectralArrow {

    public CraftSpectralArrow(CraftServer server, SpectralArrowEntity entity) {
        super(server, entity);
    }

    @Override
    public SpectralArrowEntity getHandle() {
        return (SpectralArrowEntity) entity;
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
