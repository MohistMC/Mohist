package org.bukkit.craftbukkit.v1_16_R2.entity;

import net.minecraft.entity.projectile.LlamaSpitEntity;
import org.bukkit.craftbukkit.v1_16_R2.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.projectiles.ProjectileSource;

public class CraftLlamaSpit extends AbstractProjectile implements LlamaSpit {

    public CraftLlamaSpit(CraftServer server, LlamaSpitEntity entity) {
        super(server, entity);
    }

    @Override
    public LlamaSpitEntity getHandle() {
        return (LlamaSpitEntity) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftLlamaSpit";
    }

    @Override
    public EntityType getType() {
        return EntityType.LLAMA_SPIT;
    }

    @Override
    public ProjectileSource getShooter() {
        return (getHandle().func_234616_v_() != null) ? (ProjectileSource) getHandle().func_234616_v_().getBukkitEntity() : null;
    }

    @Override
    public void setShooter(ProjectileSource source) {
        getHandle().setShooter((source != null) ? ((CraftLivingEntity) source).getHandle() : null);
    }
}
