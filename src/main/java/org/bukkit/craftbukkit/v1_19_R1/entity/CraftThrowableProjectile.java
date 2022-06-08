package org.bukkit.craftbukkit.v1_19_R1.entity;

import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.entity.ThrowableProjectile;
import org.bukkit.inventory.ItemStack;

public abstract class CraftThrowableProjectile extends CraftProjectile implements ThrowableProjectile {

    public CraftThrowableProjectile(CraftServer server, net.minecraft.world.entity.projectile.ThrowableItemProjectile entity) {
        super(server, entity);
    }

    @Override
    public ItemStack getItem() {
        if (getHandle().getItem().isEmpty()) {
            return CraftItemStack.asBukkitCopy(new net.minecraft.world.item.ItemStack(getHandle().getDefaultItemPublic()));
        } else {
            return CraftItemStack.asBukkitCopy(getHandle().getItem());
        }
    }

    @Override
    public void setItem(ItemStack item) {
        getHandle().setItem(CraftItemStack.asNMSCopy(item));
    }

    @Override
    public net.minecraft.world.entity.projectile.ThrowableItemProjectile getHandle() {
        return (net.minecraft.world.entity.projectile.ThrowableItemProjectile) entity;
    }
}
