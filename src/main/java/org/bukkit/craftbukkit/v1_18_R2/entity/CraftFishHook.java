package org.bukkit.craftbukkit.v1_18_R2.entity;

import net.minecraft.core.BlockPos;
import org.apache.commons.lang3.Validate;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FishHook;

public class CraftFishHook extends CraftProjectile implements FishHook {
    private double biteChance = -1;

    public CraftFishHook(CraftServer server, net.minecraft.world.entity.projectile.FishingHook entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.projectile.FishingHook getHandle() {
        return (net.minecraft.world.entity.projectile.FishingHook) entity;
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
        net.minecraft.world.entity.projectile.FishingHook hook = getHandle();
        Validate.isTrue(minWaitTime >= 0 && minWaitTime <= this.getMaxWaitTime(), "The minimum wait time should be between 0 and the maximum wait time.");
        hook.minWaitTime = minWaitTime;
    }

    @Override
    public int getMaxWaitTime() {
        return getHandle().maxWaitTime;
    }

    @Override
    public void setMaxWaitTime(int maxWaitTime) {
        net.minecraft.world.entity.projectile.FishingHook hook = getHandle();
        Validate.isTrue(maxWaitTime >= 0 && maxWaitTime >= this.getMinWaitTime(), "The maximum wait time should be higher than or equal to 0 and the minimum wait time.");
        hook.maxWaitTime = maxWaitTime;
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
        net.minecraft.world.entity.projectile.FishingHook hook = getHandle();

        if (this.biteChance == -1) {
            if (hook.level.isRainingAt(new BlockPos(Math.floor(hook.getX()), Math.floor(hook.getY()) + 1, Math.floor(hook.getZ())))) {
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

    @Override
    public boolean isInOpenWater() {
        return getHandle().isOpenWaterFishing();
    }

    @Override
    public Entity getHookedEntity() {
        net.minecraft.world.entity.Entity hooked = getHandle().hookedIn;
        return (hooked != null) ? hooked.getBukkitEntity() : null;
    }

    @Override
    public void setHookedEntity(Entity entity) {
        net.minecraft.world.entity.projectile.FishingHook hook = getHandle();

        hook.hookedIn = (entity != null) ? ((CraftEntity) entity).getHandle() : null;
        hook.getEntityData().set(net.minecraft.world.entity.projectile.FishingHook.DATA_HOOKED_ENTITY, hook.hookedIn != null ? hook.hookedIn.getId() + 1 : 0);
    }

    @Override
    public boolean pullHookedEntity() {
        net.minecraft.world.entity.projectile.FishingHook hook = getHandle();
        if (hook.hookedIn == null) {
            return false;
        }

        hook.pullEntity(hook.hookedIn);
        return true;
    }

    @Override
    public HookState getState() {
        return HookState.values()[0]; // Mohist TODO
    }
}
