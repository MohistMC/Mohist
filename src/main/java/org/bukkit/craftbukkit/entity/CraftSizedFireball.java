package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.projectile.AbstractFireballEntity;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.SizedFireball;
import org.bukkit.inventory.ItemStack;

public class CraftSizedFireball extends CraftFireball implements SizedFireball {

    public CraftSizedFireball(CraftServer server, AbstractFireballEntity entity) {
        super(server, entity);
    }

    @Override
    public ItemStack getDisplayItem() {
        if (getHandle().getStack().isEmpty()) {
            return new ItemStack(Material.FIRE_CHARGE);
        } else {
            return CraftItemStack.asBukkitCopy(getHandle().getStack());
        }
    }

    @Override
    public void setDisplayItem(ItemStack item) {
        getHandle().setStack(CraftItemStack.asNMSCopy(item));
    }

    @Override
    public AbstractFireballEntity getHandle() {
        return (AbstractFireballEntity) entity;
    }
}
