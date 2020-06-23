package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class CraftItem extends CraftEntity implements Item {
    private final ItemEntity item;

    public CraftItem(CraftServer server, Entity entity, ItemEntity item) {
        super(server, entity);
        this.item = item;
    }

    public CraftItem(CraftServer server, ItemEntity entity) {
        this(server, entity, entity);
    }

    @Override
    public ItemStack getItemStack() {
        return CraftItemStack.asCraftMirror(item.getItem());
    }

    @Override
    public void setItemStack(ItemStack stack) {
        item.setItem(CraftItemStack.asNMSCopy(stack));
    }

    @Override
    public int getPickupDelay() {
        return item.pickupDelay;
    }

    @Override
    public void setPickupDelay(int delay) {
        item.pickupDelay = Math.min(delay, Short.MAX_VALUE);
    }

    @Override
    public void setTicksLived(int value) {
        super.setTicksLived(value);

        // Second field for ItemEntity
        item.age = value;
    }

    @Override
    public String toString() {
        return "CraftItem";
    }

    @Override
    public EntityType getType() {
        return EntityType.DROPPED_ITEM;
    }
}
