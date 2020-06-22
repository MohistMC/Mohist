package org.bukkit.craftbukkit.v1_15_R1.entity;

import net.minecraft.entity.projectile.AbstractFireballEntity;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.SizedFireball;
import org.bukkit.inventory.ItemStack;

public class CraftSizedFireball extends CraftFireball implements SizedFireball {

    public CraftSizedFireball(CraftServer server, AbstractFireballEntity entity) {
        super(server, entity);
    }

    @Override
    public ItemStack getDisplayItem() {
        if (getHandle().getStack().isEmpty()) { // PAIL rename getItem
            return new ItemStack(Material.FIRE_CHARGE);
        } else {
            return CraftItemStack.asBukkitCopy(getHandle().getStack()); // PAIL rename getItem
        }
    }

    @Override
    public void setDisplayItem(ItemStack item) {
        getHandle().setStack(CraftItemStack.asNMSCopy(item)); // PAIL rename setItem
    }

    @Override
    public AbstractFireballEntity getHandle() {
        return (AbstractFireballEntity) entity;
    }
}
