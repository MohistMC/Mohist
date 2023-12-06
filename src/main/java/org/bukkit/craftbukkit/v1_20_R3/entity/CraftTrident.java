package org.bukkit.craftbukkit.v1_20_R3.entity;

import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.entity.Trident;
import org.bukkit.inventory.ItemStack;

public class CraftTrident extends CraftArrow implements Trident {

    public CraftTrident(CraftServer server, net.minecraft.world.entity.projectile.ThrownTrident entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.projectile.ThrownTrident getHandle() {
        return (net.minecraft.world.entity.projectile.ThrownTrident) super.getHandle();
    }

    @Override
    public ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(getHandle().pickupItemStack);
    }

    @Override
    public void setItem(ItemStack itemStack) {
        getHandle().pickupItemStack = CraftItemStack.asNMSCopy(itemStack);
    }

    @Override
    public String toString() {
        return "CraftTrident";
    }
}
