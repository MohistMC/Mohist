package org.bukkit.craftbukkit.v1_15_R1.entity;

import net.minecraft.entity.projectile.ProjectileItemEntity;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.ThrowableProjectile;
import org.bukkit.inventory.ItemStack;

public abstract class CraftThrowableProjectile extends CraftProjectile implements ThrowableProjectile {

    public CraftThrowableProjectile(CraftServer server, ProjectileItemEntity entity) {
        super(server, entity);
    }

    @Override
    public ItemStack getItem() {
        if (getHandle().func_213882_k().isEmpty()) {
            return CraftItemStack.asBukkitCopy(new net.minecraft.item.ItemStack(getHandle().getDefaultItem()));
        } else {
            return CraftItemStack.asBukkitCopy(getHandle().func_213882_k());
        }
    }

    @Override
    public void setItem(ItemStack item) {
        getHandle().setItem(CraftItemStack.asNMSCopy(item));
    }

    @Override
    public ProjectileItemEntity getHandle() {
        return (ProjectileItemEntity) entity;
    }
}
