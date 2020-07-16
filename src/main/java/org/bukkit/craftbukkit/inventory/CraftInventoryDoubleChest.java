package org.bukkit.craftbukkit.inventory;

import net.minecraft.block.ChestBlock;
import net.minecraft.container.NameableContainerFactory;
import org.bukkit.Location;
import org.bukkit.block.DoubleChest;
import org.bukkit.craftbukkit.subclasses.DoubleInventory;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryDoubleChest extends CraftInventory implements DoubleChestInventory {
    public NameableContainerFactory tile;
    private final CraftInventory left;
    private final CraftInventory right;

    public CraftInventoryDoubleChest(DoubleInventory block) {
        super(block.inventorylargechest);
        this.tile = block;
        this.left = new CraftInventory(block.inventorylargechest.first);
        this.right = new CraftInventory(block.inventorylargechest.second);
    }

    public CraftInventoryDoubleChest(net.minecraft.inventory.DoubleInventory largeChest) {
        super(largeChest);
        if (largeChest.first instanceof DoubleInventory) {
            left = new CraftInventoryDoubleChest((DoubleInventory) largeChest.first);
        } else {
            left = new CraftInventory(largeChest.first);
        }
        if (largeChest.second instanceof DoubleInventory) {
            right = new CraftInventoryDoubleChest((DoubleInventory) largeChest.second);
        } else {
            right = new CraftInventory(largeChest.second);
        }
    }

    @Override
    public Inventory getLeftSide() {
        return left;
    }

    @Override
    public Inventory getRightSide() {
        return right;
    }

    @Override
    public void setContents(ItemStack[] items) {
        if (getInventory().getInvSize() < items.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + getInventory().getInvSize() + " or less");
        }
        ItemStack[] leftItems = new ItemStack[left.getSize()], rightItems = new ItemStack[right.getSize()];
        System.arraycopy(items, 0, leftItems, 0, Math.min(left.getSize(), items.length));
        left.setContents(leftItems);
        if (items.length >= left.getSize()) {
            System.arraycopy(items, left.getSize(), rightItems, 0, Math.min(right.getSize(), items.length - left.getSize()));
            right.setContents(rightItems);
        }
    }

    @Override
    public DoubleChest getHolder() {
        return new DoubleChest(this);
    }

    @Override
    public Location getLocation() {
        return getLeftSide().getLocation().add(getRightSide().getLocation()).multiply(0.5);
    }
}
