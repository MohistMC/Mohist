package org.bukkit.craftbukkit.v1_18_R2;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;

public class CraftWorldBorder implements WorldBorder {

    private final World world;
    private final net.minecraft.world.level.border.WorldBorder handle;

    public CraftWorldBorder(CraftWorld world) {
        this.world = world;
        this.handle = world.getHandle().getWorldBorder();
    }

    public CraftWorldBorder(net.minecraft.world.level.border.WorldBorder handle) {
        this.world = null;
        this.handle = handle;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public void reset() {
        this.setSize(6.0E7D);
        this.setDamageAmount(0.2D);
        this.setDamageBuffer(5.0D);
        this.setWarningDistance(5);
        this.setWarningTime(15);
        this.setCenter(0, 0);
    }

    @Override
    public double getSize() {
        return this.handle.getSize();
    }

    @Override
    public void setSize(double newSize) {
        this.setSize(newSize, 0L);
    }

    @Override
    public void setSize(double newSize, long time) {
        // PAIL: TODO: Magic Values
        newSize = Math.min(6.0E7D, Math.max(1.0D, newSize));
        time = Math.min(9223372036854775L, Math.max(0L, time));

        if (time > 0L) {
            this.handle.lerpSizeBetween(this.handle.getSize(), newSize, time * 1000L);
        } else {
            this.handle.setSize(newSize);
        }
    }

    @Override
    public Location getCenter() {
        double x = this.handle.getCenterX();
        double z = this.handle.getCenterZ();

        return new Location(this.world, x, 0, z);
    }

    @Override
    public void setCenter(double x, double z) {
        // PAIL: TODO: Magic Values
        x = Math.min(3.0E7D, Math.max(-3.0E7D, x));
        z = Math.min(3.0E7D, Math.max(-3.0E7D, z));

        this.handle.setCenter(x, z);
    }

    @Override
    public void setCenter(Location location) {
        this.setCenter(location.getX(), location.getZ());
    }

    @Override
    public double getDamageBuffer() {
        return this.handle.getDamageSafeZone();
    }

    @Override
    public void setDamageBuffer(double blocks) {
        this.handle.setDamageSafeZone(blocks);
    }

    @Override
    public double getDamageAmount() {
        return this.handle.getDamagePerBlock();
    }

    @Override
    public void setDamageAmount(double damage) {
        this.handle.setDamagePerBlock(damage);
    }

    @Override
    public int getWarningTime() {
        return this.handle.getWarningTime();
    }

    @Override
    public void setWarningTime(int time) {
        this.handle.setWarningTime(time);
    }

    @Override
    public int getWarningDistance() {
        return this.handle.getWarningBlocks();
    }

    @Override
    public void setWarningDistance(int distance) {
        this.handle.setWarningBlocks(distance);
    }

    @Override
    public boolean isInside(Location location) {
        Preconditions.checkArgument(location != null, "location");

        return (world == null || location.getWorld().equals(this.world)) && this.handle.isWithinBounds(new BlockPos(location.getX(), location.getY(), location.getZ()));
    }

    public net.minecraft.world.level.border.WorldBorder getHandle() {
        return handle;
    }

    public boolean isVirtual() {
        return world == null;
    }
}
