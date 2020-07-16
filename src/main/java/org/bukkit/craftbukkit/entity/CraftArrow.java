package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang.Validate;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.projectiles.ProjectileSource;

public class CraftArrow extends AbstractProjectile implements AbstractArrow {

    public CraftArrow(CraftServer server, ProjectileEntity entity) {
        super(server, entity);
    }

    @Override
    public void setKnockbackStrength(int knockbackStrength) {
        Validate.isTrue(knockbackStrength >= 0, "Knockback cannot be negative");
        getHandle().setPunch(knockbackStrength);
    }

    @Override
    public int getKnockbackStrength() {
        return getHandle().punch;
    }

    @Override
    public double getDamage() {
        return getHandle().getDamage();
    }

    @Override
    public void setDamage(double damage) {
        Preconditions.checkArgument(damage >= 0, "Damage must be positive");
        getHandle().setDamage(damage);
    }

    @Override
    public int getPierceLevel() {
        return getHandle().getPierceLevel();
    }

    @Override
    public void setPierceLevel(int pierceLevel) {
        Preconditions.checkArgument(0 <= pierceLevel && pierceLevel <= Byte.MAX_VALUE, "Pierce level out of range, expected 0 < level < 127");

        getHandle().setPierceLevel((byte) pierceLevel);
    }

    @Override
    public boolean isCritical() {
        return getHandle().isCritical();
    }

    @Override
    public void setCritical(boolean critical) {
        getHandle().setCritical(critical);
    }

    @Override
    public ProjectileSource getShooter() {
        return getHandle().projectileSource;
    }

    @Override
    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof Entity) {
            getHandle().setOwner(((CraftEntity) shooter).getHandle());
        } else {
            getHandle().setOwner(null);
        }
        getHandle().projectileSource = shooter;
    }

    @Override
    public boolean isInBlock() {
        return getHandle().inGround;
    }

    @Override
    public Block getAttachedBlock() {
        if (!isInBlock()) {
            return null;
        }

        BlockPos pos = getHandle().getBlockPos();
        return getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public PickupStatus getPickupStatus() {
        return PickupStatus.values()[getHandle().pickupType.ordinal()];
    }

    @Override
    public void setPickupStatus(PickupStatus status) {
        Preconditions.checkNotNull(status, "status");
        getHandle().pickupType = ProjectileEntity.PickupPermission.fromOrdinal(status.ordinal());
    }

    @Override
    public boolean isShotFromCrossbow() {
        throw new RuntimeException("i was too lazy to implement this");
    }

    @Override
    public void setShotFromCrossbow(boolean shotFromCrossbow) {
        throw new RuntimeException("i was too lazy to implement this");
    }

    @Override
    public void setTicksLived(int value) {
        super.setTicksLived(value);

        // Second field for EntityArrow
        getHandle().life = value;
    }

    @Override
    public ProjectileEntity getHandle() {
        return (ProjectileEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftArrow";
    }

    @Override
    public EntityType getType() {
        return EntityType.UNKNOWN;
    }
}
