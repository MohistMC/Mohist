package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FishHook;

public class CraftFishHook extends CraftProjectile implements FishHook {
    private double biteChance = -1;

    public CraftFishHook(CraftServer server, FishingBobberEntity entity) {
        super(server, entity);
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
    public int getMinWaitTime() {
        return getHandle().minWaitTime;
    }

    @Override
    public void setMinWaitTime(int minWaitTime) {
        FishingBobberEntity hook = getHandle();
        Validate.isTrue(minWaitTime >= 0 && minWaitTime <= this.getMaxWaitTime(), "The minimum wait time should be between 0 and the maximum wait time.");
        hook.minWaitTime = minWaitTime;
    }

    @Override
    public int getMaxWaitTime() {
        return getHandle().maxWaitTime;
    }

    @Override
    public void setMaxWaitTime(int maxWaitTime) {
        FishingBobberEntity hook = getHandle();
        Validate.isTrue(maxWaitTime >= 0 && maxWaitTime >= this.getMinWaitTime(), "The maximum wait time should higher than 0 and the minimum wait time.");
        hook.minWaitTime = maxWaitTime;
    }

    @Override
    public boolean getApplyLure() {
        return getHandle().applyLure;
    }

    @Override
    public void setApplyLure(boolean applyLure) {
        getHandle().applyLure = applyLure;
    }

    @Override
    public double getBiteChance() {
        FishingBobberEntity hook = getHandle();

        if (this.biteChance == -1) {
            if (hook.world.isRainingAt(new BlockPos(MathHelper.floor(hook.getPosX()), MathHelper.floor(hook.getPosY()) + 1, MathHelper.floor(hook.getPosZ())))) {
                return 1 / 300.0;
            }
            return 1 / 500.0;
        }
        return this.biteChance;
    }

    @Override
    public void setBiteChance(double chance) {
        Validate.isTrue(chance >= 0 && chance <= 1, "The bite chance must be between 0 and 1.");
        this.biteChance = chance;
    }
}
