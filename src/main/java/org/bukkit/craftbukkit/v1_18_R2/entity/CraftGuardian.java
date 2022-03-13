package org.bukkit.craftbukkit.v1_18_R2.entity;

import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.LivingEntity;

public class CraftGuardian extends CraftMonster implements Guardian {

    public CraftGuardian(CraftServer server, net.minecraft.world.entity.monster.Guardian entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.Guardian getHandle() {
        return (net.minecraft.world.entity.monster.Guardian) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftGuardian";
    }

    @Override
    public EntityType getType() {
        return EntityType.GUARDIAN;
    }

    @Override
    public void setTarget(LivingEntity target) {
        super.setTarget(target);

        // clean up laser target, when target is removed
        if (target == null) {
            getHandle().setActiveAttackTarget(0);
        }
    }

    @Override
    public boolean setLaser(boolean activated) {
        if (activated) {
            LivingEntity target = getTarget();
            if (target == null) {
                return false;
            }

            getHandle().setActiveAttackTarget(target.getEntityId());
        } else {
            getHandle().setActiveAttackTarget(0);
        }

        return true;
    }

    @Override
    public boolean hasLaser() {
        return getHandle().hasActiveAttackTarget();
    }

    @Override
    public boolean isElder() {
        return false;
    }

    @Override
    public void setElder(boolean shouldBeElder) {
        throw new UnsupportedOperationException("Not supported.");
    }
}
