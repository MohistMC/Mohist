package org.bukkit.craftbukkit.v1_20_R3.inventory;

import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_20_R3.util.CraftNamespacedKey;
import org.bukkit.inventory.ComplexRecipe;
import org.bukkit.inventory.ItemStack;

public class CraftComplexRecipe implements CraftRecipe, ComplexRecipe {

    private final NamespacedKey key;
    private final CustomRecipe recipe;

    public CraftComplexRecipe(NamespacedKey key, CustomRecipe recipe) {
        this.key = key;
        this.recipe = recipe;
    }

    @Override
    public ItemStack getResult() {
        return CraftItemStack.asCraftMirror(recipe.getResultItem(RegistryAccess.EMPTY));
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @Override
    public void addToCraftingManager() {
        MinecraftServer.getServer().getRecipeManager().addRecipe(new RecipeHolder<>(CraftNamespacedKey.toMinecraft(key), recipe));
    }
}
