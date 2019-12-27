package org.bukkit.craftbukkit.inventory;

import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.server.MinecraftServer;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.inventory.ComplexRecipe;
import org.bukkit.inventory.ItemStack;

public class CraftComplexRecipe implements CraftRecipe, ComplexRecipe {

    private final SpecialRecipe recipe;

    public CraftComplexRecipe(SpecialRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public ItemStack getResult() {
        return CraftItemStack.asCraftMirror(recipe.getResult());
    }

    @Override
    public NamespacedKey getKey() {
        return CraftNamespacedKey.fromMinecraft(recipe.getKey());
    }

    @Override
    public void addToCraftingManager() {
        MinecraftServer.getServer().getCraftingManager().addRecipe(recipe);
    }
}
