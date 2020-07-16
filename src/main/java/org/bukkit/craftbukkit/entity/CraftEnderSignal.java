package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.entity.EnderEyeEntity;
import net.minecraft.util.math.BlockPos;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.EntityType;

public class CraftEnderSignal extends CraftEntity implements EnderSignal {
    public CraftEnderSignal(CraftServer server, EnderEyeEntity entity) {
        super(server, entity);
    }

    @Override
    public EnderEyeEntity getHandle() {
        return (EnderEyeEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftEnderSignal";
    }

    @Override
    public EntityType getType() {
        return EntityType.ENDER_SIGNAL;
    }

    @Override
    public Location getTargetLocation() {
        return new Location(getWorld(), getHandle().velocityX, getHandle().velocityY, getHandle().velocityZ, getHandle().yaw, getHandle().pitch);
    }

    @Override
    public void setTargetLocation(Location location) {
        Preconditions.checkArgument(getWorld().equals(location.getWorld()), "Cannot target EnderSignal across worlds");
        getHandle().moveTowards(new BlockPos(location.getX(), location.getY(), location.getZ()));
    }

    @Override
    public boolean getDropItem() {
        return getHandle().dropsItem;
    }

    @Override
    public void setDropItem(boolean shouldDropItem) {
        getHandle().dropsItem = shouldDropItem;
    }

    @Override
    public int getDespawnTimer() {
        return getHandle().useCount;
    }

    @Override
    public void setDespawnTimer(int time) {
        getHandle().useCount = time;
    }
}
