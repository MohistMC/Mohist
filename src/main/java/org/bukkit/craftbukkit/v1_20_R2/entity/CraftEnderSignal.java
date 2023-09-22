package org.bukkit.craftbukkit.v1_20_R2.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.item.Items;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftLocation;
import org.bukkit.entity.EnderSignal;
import org.bukkit.inventory.ItemStack;

public class CraftEnderSignal extends CraftEntity implements EnderSignal {
    public CraftEnderSignal(CraftServer server, net.minecraft.world.entity.projectile.EyeOfEnder entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.projectile.EyeOfEnder getHandle() {
        return (net.minecraft.world.entity.projectile.EyeOfEnder) entity;
    }

    @Override
    public String toString() {
        return "CraftEnderSignal";
    }

    @Override
    public Location getTargetLocation() {
        return new Location(getWorld(), getHandle().tx, getHandle().ty, getHandle().tz, getHandle().getYRot(), getHandle().getXRot());
    }

    @Override
    public void setTargetLocation(Location location) {
        Preconditions.checkArgument(getWorld().equals(location.getWorld()), "Cannot target EnderSignal across worlds");
        getHandle().signalTo(CraftLocation.toBlockPosition(location));
    }

    @Override
    public boolean getDropItem() {
        return getHandle().surviveAfterDeath;
    }

    @Override
    public void setDropItem(boolean shouldDropItem) {
        getHandle().surviveAfterDeath = shouldDropItem;
    }

    @Override
    public ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(getHandle().getItem());
    }

    @Override
    public void setItem(ItemStack item) {
        getHandle().setItem(item != null ? CraftItemStack.asNMSCopy(item) : Items.ENDER_EYE.getDefaultInstance());
    }

    @Override
    public int getDespawnTimer() {
        return getHandle().life;
    }

    @Override
    public void setDespawnTimer(int time) {
        getHandle().life = time;
    }
}
