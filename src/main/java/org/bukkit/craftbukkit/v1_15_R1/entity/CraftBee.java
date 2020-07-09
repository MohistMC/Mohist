package org.bukkit.craftbukkit.v1_15_R1.entity;

import com.google.common.base.Preconditions;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.passive.BeeEntity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
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
        BlockPos hive = getHandle().getHivePos();
        return (hive == null) ? null : new Location(getWorld(), hive.getX(), hive.getY(), hive.getZ());
    }

    @Override
    public void setHive(Location location) {
        Preconditions.checkArgument(location == null || this.getWorld().equals(location.getWorld()), "Hive must be in same world");
        getHandle().hivePos = (location == null) ? null : new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Override
    public Location getFlower() {
        BlockPos flower = getHandle().getFlowerPos();
        return (flower == null) ? null : new Location(getWorld(), flower.getX(), flower.getY(), flower.getZ());
    }

    @Override
    public void setFlower(Location location) {
        Preconditions.checkArgument(location == null || this.getWorld().equals(location.getWorld()), "Flower must be in same world");
        getHandle().setFlowerPos(location == null ? null : new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
    }

    @Override
    public boolean hasNectar() {
        return getHandle().hasNectar();
    }

    @Override
    public void setHasNectar(boolean nectar) {
        getHandle().setHasNectar(nectar);
    }

    @Override
    public boolean hasStung() {
        return getHandle().hasStung();
    }

    @Override
    public void setHasStung(boolean stung) {
        getHandle().setHasStung(stung);
    }

    @Override
    public int getAnger() {
        return getHandle().getAnger();
    }

    @Override
    public void setAnger(int anger) {
        getHandle().setAnger(anger);
    }

    @Override
    public int getCannotEnterHiveTicks() {
        return getHandle().stayOutOfHiveCountdown;
    }

    @Override
    public void setCannotEnterHiveTicks(int ticks) {
        getHandle().setStayOutOfHiveCountdown(ticks);
    }
}
