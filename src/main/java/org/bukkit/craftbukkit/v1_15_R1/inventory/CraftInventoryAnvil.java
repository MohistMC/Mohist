package org.bukkit.craftbukkit.v1_15_R1.inventory;

import com.google.common.base.Preconditions;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.inventory.IInventory;
import org.bukkit.Location;
import org.bukkit.inventory.AnvilInventory;

public class CraftInventoryAnvil extends CraftResultInventory implements AnvilInventory {

    private final Location location;
    private final RepairContainer container;

    public CraftInventoryAnvil(Location location, IInventory inventory, IInventory resultInventory, RepairContainer container) {
        super(inventory, resultInventory);
        this.location = location;
        this.container = container;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public String getRenameText() {
        return container.repairedItemName;
    }

    @Override
    public int getRepairCost() {
        return container.maximumCost.get();
    }

    @Override
    public void setRepairCost(int i) {
        container.maximumCost.set(i);
    }

    @Override
    public int getMaximumRepairCost() {
        return container.maximumRepairCost;
    }

    @Override
    public void setMaximumRepairCost(int levels) {
        Preconditions.checkArgument(levels >= 0, "Maximum repair cost must be positive (or 0)");
        container.maximumRepairCost = levels;
    }
}
