package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import net.minecraft.container.AnvilContainer;
import net.minecraft.inventory.Inventory;
import org.bukkit.Location;
import org.bukkit.inventory.AnvilInventory;

public class CraftInventoryAnvil extends CraftResultInventory implements AnvilInventory {

    private final Location location;
    private final AnvilContainer container;

    public CraftInventoryAnvil(Location location, Inventory inventory, Inventory resultInventory, AnvilContainer container) {
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
        return container.newItemName;
    }

    @Override
    public int getRepairCost() {
        return container.levelCost.get();
    }

    @Override
    public void setRepairCost(int i) {
        container.levelCost.set(i);
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
