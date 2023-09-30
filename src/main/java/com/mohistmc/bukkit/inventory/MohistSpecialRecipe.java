package com.mohistmc.bukkit.inventory;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftComplexRecipe;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/20 23:02:32
 */
public class MohistSpecialRecipe extends CraftComplexRecipe {

    private final Recipe<?> recipe;

    public MohistSpecialRecipe(NamespacedKey id, Recipe<?> recipe) {
        super(id, null);
        this.recipe = recipe;
    }

    @Override
    public @NotNull ItemStack getResult() {
        return CraftItemStack.asCraftMirror(this.recipe.getResultItem(ServerLifecycleHooks.getCurrentServer().registryAccess()));
    }

    @Override
    public void addToCraftingManager() {
        ServerLifecycleHooks.getCurrentServer().getRecipeManager().addRecipe(new RecipeHolder<>(CraftNamespacedKey.toMinecraft(this.getKey()), this.recipe));
    }
}
