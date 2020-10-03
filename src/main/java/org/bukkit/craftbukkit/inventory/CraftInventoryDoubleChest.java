package org.bukkit.craftbukkit.inventory;

import net.minecraft.block.ChestBlock;
import net.minecraft.inventory.DoubleSidedInventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import org.bukkit.Location;
import org.bukkit.block.DoubleChest;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryDoubleChest extends CraftInventory implements DoubleChestInventory {
    public INamedContainerProvider tile;
    private final CraftInventory left;
    private final CraftInventory right;

    public CraftInventoryDoubleChest(ChestBlock.DoubleInventory block) {
        super(block.inventorylargechest);
        this.tile = block;
        this.left = new CraftInventory(block.inventorylargechest.upperChest);
        this.right = new CraftInventory(block.inventorylargechest.lowerChest);
    }

    public CraftInventoryDoubleChest(DoubleSidedInventory largeChest) {
        super(largeChest);
        if (largeChest.upperChest instanceof DoubleSidedInventory) {
            left = new CraftInventoryDoubleChest((DoubleSidedInventory) largeChest.upperChest);
        } else {
            left = new CraftInventory(largeChest.upperChest);
        }
        if (largeChest.lowerChest instanceof DoubleSidedInventory) {
            right = new CraftInventoryDoubleChest((DoubleSidedInventory) largeChest.lowerChest);
        } else {
            right = new CraftInventory(largeChest.lowerChest);
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
        if (getInventory().getSizeInventory() < items.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + getInventory().getSizeInventory() + " or less");
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
