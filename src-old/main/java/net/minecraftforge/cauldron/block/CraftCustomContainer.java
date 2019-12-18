package net.minecraftforge.cauldron.block;

import net.minecraft.inventory.IInventory;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlockState;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CraftCustomContainer extends CraftBlockState implements InventoryHolder {
    private final net.minecraft.inventory.IInventory container;

    public CraftCustomContainer(Block block) {
        super(block);
        container = (IInventory) ((CraftWorld) getWorld()).getTileEntityAt(getX(), getY(), getZ());
    }

    @Override
    public Inventory getInventory() {
        CraftInventory inventory = new CraftInventory(container);
        return inventory;
    }
}
