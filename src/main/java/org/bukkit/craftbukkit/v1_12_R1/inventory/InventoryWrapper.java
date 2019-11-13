package org.bukkit.craftbukkit.v1_12_R1.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftChatMessage;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class InventoryWrapper implements IInventory {

    private final Inventory inventory;
    private final List<HumanEntity> viewers;

    public InventoryWrapper(Inventory inventory) {
        this.inventory = inventory;
        this.viewers = new ArrayList<>();
    }

    @Override
    public int getSizeInventory() {
        return inventory.getSize();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return CraftItemStack.asNMSCopy(inventory.getItem(i));
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        // Copied from CraftItemStack
        ItemStack stack = getStackInSlot(i);
        ItemStack result;
        if (stack.isEmpty()) {
            return stack;
        }
        if (stack.getCount() <= j) {
            this.setInventorySlotContents(i, ItemStack.EMPTY);
            result = stack;
        } else {
            result = CraftItemStack.copyNMSStack(stack, j);
            stack.shrink(j);
        }
        this.markDirty();
        return result;
    }

    @Override
    public ItemStack removeStackFromSlot(int i) {
        // Copied from CraftItemStack
        ItemStack stack = getStackInSlot(i);
        ItemStack result;
        if (stack.isEmpty()) {
            return stack;
        }
        if (stack.getCount() <= 1) {
            this.setInventorySlotContents(i, ItemStack.EMPTY);
            result = stack;
        } else {
            result = CraftItemStack.copyNMSStack(stack, 1);
            stack.shrink(1);
        }
        return result;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        inventory.setItem(i, CraftItemStack.asBukkitCopy(itemstack));
    }

    @Override
    public int getInventoryStackLimit() {
        return inventory.getMaxStackSize();
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityhuman) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer entityhuman) {
    }

    @Override
    public void closeInventory(EntityPlayer entityhuman) {
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    @Override
    public int getField(int i) {
        return 0;
    }

    @Override
    public void setField(int i, int j) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public List<ItemStack> getContents() {
        int size = getSizeInventory();
        List<ItemStack> items = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            items.set(i, getStackInSlot(i));
        }

        return items;
    }

    @Override
    public void onOpen(final CraftHumanEntity who) {
        this.viewers.add(who);
    }

    @Override
    public void onClose(final CraftHumanEntity who) {
        this.viewers.remove(who);
    }

    @Override
    public List<HumanEntity> getViewers() {
        return this.viewers;
    }

    @Override
    public InventoryHolder getOwner() {
        return inventory.getHolder();
    }

    @Override
    public void setMaxStackSize(int size) {
        inventory.setMaxStackSize(size);
    }

    @Override
    public String getName() {
        return inventory.getName();
    }

    @Override
    public boolean hasCustomName() {
        return getName() != null;
    }

    @Override
    public ITextComponent getDisplayName() {
        return CraftChatMessage.fromString(getName())[0];
    }

    @Override
    public Location getLocation() {
        return inventory.getLocation();
    }

    @Override
    public boolean isEmpty() {
        return StreamSupport.stream(inventory.spliterator(), false).anyMatch(Objects::nonNull);
    }
}
