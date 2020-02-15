package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.passive.BeeEntity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Bee;
import org.bukkit.entity.EntityType;

public class CraftBee extends CraftAnimals implements Bee {

    public CraftBee(CraftServer server, BeeEntity entity) {
        super(server, entity);
    }

    @Override
    public BeeEntity getHandle() {
        return (BeeEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftBee";
    }

    @Override
    public EntityType getType() {
        return EntityType.BEE;
    }

    @Override
    public Location getHive() {
        BlockPos hive = getHandle().func_226410_eB_();
        return (hive == null) ? null : new Location(getWorld(), hive.getX(), hive.getY(), hive.getZ());
    }

    @Override
    public void setHive(Location location) {
        Preconditions.checkArgument(location == null || this.getWorld().equals(location.getWorld()), "Hive must be in same world");
        getHandle().field_226369_bI_ = (location == null) ? null : new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Override
    public Location getFlower() {
        BlockPos flower = getHandle().func_226424_eq_();
        return (flower == null) ? null : new Location(getWorld(), flower.getX(), flower.getY(), flower.getZ());
    }

    @Override
    public void setFlower(Location location) {
        Preconditions.checkArgument(location == null || this.getWorld().equals(location.getWorld()), "Flower must be in same world");
        getHandle().func_226431_g_(location == null ? null : new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
    }

    @Override
    public boolean hasNectar() {
        return getHandle().func_226411_eD_();
    }

    @Override
    public void setHasNectar(boolean nectar) {
        getHandle().func_226447_r_(nectar);
    }

    @Override
    public boolean hasStung() {
        return getHandle().func_226412_eE_();
    }

    @Override
    public void setHasStung(boolean stung) {
        getHandle().func_226449_s_(stung);
    }

    @Override
    public int getAnger() {
        return getHandle().func_226418_eL_();
    }

    @Override
    public void setAnger(int anger) {
        getHandle().func_226453_u_(anger);
    }

    @Override
    public int getCannotEnterHiveTicks() {
        return getHandle().field_226364_bD_;
    }

    @Override
    public void setCannotEnterHiveTicks(int ticks) {
        getHandle().func_226450_t_(ticks);
    }
}
