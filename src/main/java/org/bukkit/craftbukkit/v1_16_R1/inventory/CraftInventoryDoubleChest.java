package org.bukkit.craftbukkit.v1_16_R1.inventory;

import org.bukkit.Location;
import org.bukkit.block.DoubleChest;
import net.minecraft.inventory.DoubleInventory;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryDoubleChest extends CraftInventory implements DoubleChestInventory {

    private final CraftInventory left;
    private final CraftInventory right;

    public CraftInventoryDoubleChest(DoubleInventory block) {
        super(block);
        this.left = new CraftInventory(block.first);
        this.right = new CraftInventory(block.second);
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
        if (getInventory().size() < items.length)
            throw new IllegalArgumentException("Invalid inventory size: expected <= " + getInventory().size());

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