package org.bukkit.craftbukkit.inventory;

import java.util.Iterator;

import net.minecraftforge.cauldron.inventory.CustomModRecipe;

import org.bukkit.inventory.Recipe;


public class RecipeIterator implements Iterator<Recipe> {
    private final Iterator<net.minecraft.item.crafting.IRecipe> recipes;
    private final Iterator<net.minecraft.item.ItemStack> smeltingCustom;
    private final Iterator<net.minecraft.item.ItemStack> smeltingVanilla;
    private Iterator<?> removeFrom = null;

    public RecipeIterator() {
        this.recipes = net.minecraft.item.crafting.CraftingManager.getInstance().getRecipeList().iterator();
        this.smeltingCustom = net.minecraft.item.crafting.FurnaceRecipes.smelting().customRecipes.keySet().iterator();
        this.smeltingVanilla = net.minecraft.item.crafting.FurnaceRecipes.smelting().smeltingList.keySet().iterator();
    }

    public boolean hasNext() {
        return recipes.hasNext() || smeltingCustom.hasNext() || smeltingVanilla.hasNext();
    }

    public Recipe next() {
        if (recipes.hasNext()) {
            removeFrom = recipes;
            // Cauldron start - handle custom recipe classes without Bukkit API equivalents
            net.minecraft.item.crafting.IRecipe iRecipe = recipes.next();
            try {
                return iRecipe.toBukkitRecipe();
            } catch (AbstractMethodError ex) {
                // No Bukkit wrapper provided
                return new CustomModRecipe(iRecipe);
            }
            // Cauldron end
        } else {
            net.minecraft.item.ItemStack item;
            if (smeltingCustom.hasNext()) {
                removeFrom = smeltingCustom;
                item = smeltingCustom.next();
            } else {
                removeFrom = smeltingVanilla;
                item = smeltingVanilla.next();
            }

            CraftItemStack stack = CraftItemStack.asCraftMirror(net.minecraft.item.crafting.FurnaceRecipes.smelting().getSmeltingResult(item));

            return new CraftFurnaceRecipe(stack, CraftItemStack.asCraftMirror(item));
        }
    }

    public void remove() {
        if (removeFrom == null) {
            throw new IllegalStateException();
        }
        removeFrom.remove();
    }
}
