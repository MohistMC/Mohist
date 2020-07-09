package org.bukkit.craftbukkit.v1_15_R1.entity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FishHook;
import org.bukkit.projectiles.ProjectileSource;

public class CraftFishHook extends AbstractProjectile implements FishHook {
    private double biteChance = -1;

    public CraftFishHook(CraftServer server, FishingBobberEntity entity) {
        super(server, entity);
    }

    @Override
    public ProjectileSource getShooter() {
        if (getHandle().angler != null) {
            return getHandle().angler.getBukkitEntity();
        }

        return null;
    }

    @Override
    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof CraftHumanEntity) {
            getHandle().angler = (PlayerEntity) ((CraftHumanEntity) shooter).entity;
        }
    }

    @Override
    public FishingBobberEntity getHandle() {
        return (FishingBobberEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftFishingHook";
    }

    @Override
    public EntityType getType() {
        return EntityType.FISHING_HOOK;
    }

    @Override
    public double getBiteChance() {
        FishingBobberEntity hook = getHandle();

        if (this.biteChance == -1) {
            if (hook.world.isRainingAt(new BlockPos(MathHelper.floor(hook.getPosX()), MathHelper.floor(hook.getPosY()) + 1, MathHelper.floor(hook.getPosZ())))) {
                return 1/300.0;
            }
            return 1/500.0;
        }
        return this.biteChance;
    }

    @Override
    public void setBiteChance(double chance) {
        Validate.isTrue(chance >= 0 && chance <= 1, "The bite chance must be between 0 and 1.");
        this.biteChance = chance;
    }
}
