package org.bukkit.craftbukkit.v1_12_R1.entity;

import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.math.BlockPos;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;

public class CraftEnderCrystal extends CraftEntity implements EnderCrystal {
    public CraftEnderCrystal(CraftServer server, EntityEnderCrystal entity) {
        super(server, entity);
    }

    @Override
    public boolean isShowingBottom() {
        return getHandle().shouldShowBottom();
    }

    @Override
    public void setShowingBottom(boolean showing) {
        getHandle().setShowBottom(showing);
    }

    @Override
    public Location getBeamTarget() {
        BlockPos pos = getHandle().getBeamTarget();
        return pos == null ? null : new Location(getWorld(), pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void setBeamTarget(Location location) {
        if (location == null) {
            getHandle().setBeamTarget(null);
        } else if (location.getWorld() != getWorld()) {
            throw new IllegalArgumentException("Cannot set beam target location to different world");
        } else {
            getHandle().setBeamTarget(new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        }
    }

    @Override
    public EntityEnderCrystal getHandle() {
        return (EntityEnderCrystal) entity;
    }

    @Override
    public String toString() {
        return "CraftEnderCrystal";
    }

    public EntityType getType() {
        return EntityType.ENDER_CRYSTAL;
    }
}
