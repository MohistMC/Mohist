package org.bukkit.craftbukkit.v1_12_R1.inventory;

import org.bukkit.inventory.Recipe;

public interface CraftRecipe extends Recipe {
    void addToCraftingManager();
}
